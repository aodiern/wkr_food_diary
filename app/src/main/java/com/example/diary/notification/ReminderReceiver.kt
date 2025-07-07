package com.example.diary.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.diary.R


class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("reminder_message") ?: return


        val channelId = "daily_reminder_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Daily Reminders"
            val desc = "Channel for daily reminders"
            val chan = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = desc
            }
            context.getSystemService(NotificationManager::class.java)
                ?.createNotificationChannel(chan)
        }


        val notif = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Напоминание")
            .setContentText(message)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify( /* уникальный ID */ message.hashCode(), notif)
    }
}