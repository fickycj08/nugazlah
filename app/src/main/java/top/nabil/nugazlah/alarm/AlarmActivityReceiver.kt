package top.nabil.nugazlah.alarm

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import top.nabil.nugazlah.MainActivity

class AlarmActivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val id = intent?.getStringExtra("ID") ?: ""

        val resultIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("for", "alarmTask")
            putExtra("taskId", id)
        }

        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        resultPendingIntent?.send()
    }
}