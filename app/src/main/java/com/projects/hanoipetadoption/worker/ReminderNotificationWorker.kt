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
    }

    override fun doWork(): Result {
        // Get input data
        val reminderId = inputData.getInt("reminderId", 0)
        val petId = inputData.getInt("petId", 0)
        val title = inputData.getString("title") ?: "Pet Reminder"
        val content = inputData.getString("content") ?: "You have a reminder for your pet"

        // Create notification channel (required for Android O and above)
        createNotificationChannel()

        // Create intent for when the notification is tapped
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("reminderId", reminderId)
            putExtra("petId", petId)
            putExtra("openReminder", true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 
            reminderId,
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
