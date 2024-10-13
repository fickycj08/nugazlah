package top.nabil.nugazlah.alarm

const val NOTIFICANTION_CHANNEL_ID = "NOTIFICANTION_NUGAZLAH"

data class AlarmData(
    val id: Int,
    val taskId: String,
    val subject: String,
    val title: String,
    val description: String,
    val deadline: String
)