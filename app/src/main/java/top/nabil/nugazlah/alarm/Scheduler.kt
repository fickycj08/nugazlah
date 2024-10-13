package top.nabil.nugazlah.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class Scheduler(
    private val context: Context
) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    // TODO alarm scheduled only when user receive the task, make it realtime with websocket or workmanager
    fun schedule(data: AlarmData, triggerAt: Long) {
        val intent = Intent(context, AlarmNotificationReceiver::class.java).apply {
            putExtra("ID", data.taskId)
            putExtra("SUBJECT", data.subject)
            putExtra("TITLE", data.title)
            putExtra("DESCRIPTION", data.description)
            putExtra("DEADLINE", data.deadline)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            data.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val currentTimeMillis = System.currentTimeMillis()

        if (triggerAt > currentTimeMillis) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        }
    }

    fun scheduleActivity(data: AlarmData, triggerAt: Long) {
        val intent = Intent(context, AlarmActivityReceiver::class.java).apply {
            putExtra("ID", data.taskId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            data.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val currentTimeMillis = System.currentTimeMillis()

        if (triggerAt > currentTimeMillis) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        }
    }
}
