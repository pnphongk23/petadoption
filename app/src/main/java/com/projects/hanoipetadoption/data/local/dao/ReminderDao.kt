package com.projects.hanoipetadoption.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.projects.hanoipetadoption.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Query("SELECT * FROM reminders WHERE id = :reminderId")
    suspend fun getReminderById(reminderId: Long): ReminderEntity?

    @Query("SELECT * FROM reminders WHERE pet_id = :petId ORDER BY due_date ASC")
    fun getRemindersForPet(petId: String): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE due_date >= :currentDate AND due_date < :endDate AND is_completed = 0 ORDER BY due_date ASC")
    fun getUpcomingReminders(currentDate: Long, endDate: Long): Flow<List<ReminderEntity>>
    
    @Query("SELECT * FROM reminders ORDER BY due_date ASC")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    @Query("UPDATE reminders SET is_completed = 1 WHERE id = :reminderId")
    suspend fun markReminderAsComplete(reminderId: Long)

    @Query("DELETE FROM reminders WHERE id = :reminderId")
    suspend fun deleteReminderById(reminderId: Long)

    @Query("DELETE FROM reminders WHERE pet_id = :petId")
    suspend fun deleteRemindersForPet(petId: String)
} 