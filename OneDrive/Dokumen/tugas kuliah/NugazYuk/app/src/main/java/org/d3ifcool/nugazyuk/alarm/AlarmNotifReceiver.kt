package org.d3ifcool.nugazyuk.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import org.d3ifcool.nugazyuk.MainActivity
import org.d3ifcool.nugazyuk.R

class AlarmNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(context)

        val id = intent?.getStringExtra("ID") ?: ""
        val subject = intent?.getStringExtra("SUBJECT") ?: ""
        val title = intent?.getStringExtra("TITLE") ?: ""
        val description = intent?.getStringExtra("DESCRIPTION") ?: ""
        val deadline = intent?.getStringExtra("DEADLINE") ?: ""

        val resultIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("for", "detailTask")
            putExtra("taskId", id)
        }

        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val soundUri: Uri =
            Uri.parse("android.resource://${context.packageName}/${R.raw.notif_sound}")

        val notification = NotificationCompat.Builder(
            context,
            NOTIFICANTION_CHANNEL_ID,
        )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.baseline_crisis_alert_24)
            .setContentTitle(context.getString(R.string.notif_title, subject, deadline))
            .setContentText(title)
            .setSound(soundUri)
            .setContentIntent(resultPendingIntent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(description)
            )
            .build()

        notificationManager.notify(1, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {
        val name = "Reminder Channel"
        val descriptionText = "Channel untuk mengingatkan tugas"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(NOTIFICANTION_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}