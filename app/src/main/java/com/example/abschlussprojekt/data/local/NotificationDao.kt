package com.example.abschlussprojekt.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.abschlussprojekt.datamodel.localmodels.EventInfoEntity
import com.example.abschlussprojekt.datamodel.localmodels.NotificationEntity
import com.example.abschlussprojekt.datamodel.localmodels.NotificationTextsEntity
import com.example.abschlussprojekt.datamodel.localmodels.ReminderSettingsEntity


@Dao
interface NotificationDao {

    @Query("SELECT * FROM notifications WHERE scheduledDateTime > :currentTime ORDER BY scheduledDateTime ASC")
    fun getFutureNotifications(currentTime: Long): List<NotificationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("DELETE FROM notifications WHERE scheduledDateTime <= :currentTime")
    fun deletePastNotifications(currentTime: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReminderSettings(settings: ReminderSettingsEntity)

    @Query("SELECT * FROM reminder_settings LIMIT 1")
    fun getReminderSettings(): ReminderSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEventInfo(eventInfo: EventInfoEntity)

    @Query("SELECT * FROM event_info LIMIT 1")
    fun getEventInfo(): EventInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotificationTexts(texts: NotificationTextsEntity)

    @Query("SELECT * FROM notification_texts LIMIT 1")
    fun getNotificationTexts(): NotificationTextsEntity?
}