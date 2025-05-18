package com.projects.hanoipetadoption.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.projects.hanoipetadoption.MainActivity
import com.projects.hanoipetadoption.R

/**
 * Worker class for handling reminder notifications
 */
class ReminderNotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {
        const val CHANNEL_ID = "pet_reminders_channel"
        const val NOTIFICATION_ID_PREFIX = 1000
        // Define keys for WorkManager input data to be accessed by ReminderLocalDataSourceImpl
        const val KEY_REMINDER_ID = "reminderId"
        const val KEY_PET_ID = "petId"
        const val KEY_TITLE = "title"
        const val KEY_CONTENT = "content"
    }

    override fun doWork(): Result {
        val reminderId = inputData.getInt(KEY_REMINDER_ID, 0)
        // Changed to getString to match the type of petId in HealthRecord/ReminderEntity
        val petId = inputData.getString(KEY_PET_ID) 
        val title = inputData.getString(KEY_TITLE) ?: "Pet Reminder"
        val content = inputData.getString(KEY_CONTENT) ?: "You have a reminder for your pet"

        if (reminderId == 0 || petId == null) {
            // Invalid input, perhaps log an error or return failure
            return Result.failure()
        }

        // Create notification channel (required for Android O and above)
        createNotificationChannel()

        // Create intent for when the notification is tapped
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(KEY_REMINDER_ID, reminderId)
            putExtra(KEY_PET_ID, petId) // Now passing String petId
            putExtra("openReminder", true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 
            reminderId, // Use reminderId for requestCode to ensure uniqueness for PendingIntent updates
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )        // Build the notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) 
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Show the notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Use a unique notification ID by combining prefix and reminderId
        notificationManager.notify(NOTIFICATION_ID_PREFIX + reminderId, builder.build())

        return Result.success()
    }    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Pet Reminders"
            val descriptionText = "Notifications for pet health checkups, vaccinations, and other care reminders"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
                enableLights(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
