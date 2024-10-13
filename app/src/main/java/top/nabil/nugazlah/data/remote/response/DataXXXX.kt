package top.nabil.nugazlah.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGetDetailTaskData(
    @SerialName("class_id")
    val classId: String,
    @SerialName("deadline")
    val deadline: String,
    @SerialName("description")
    val description: String,
    @SerialName("detail")
    val detail: String,
    @SerialName("id")
    val id: String,
    @SerialName("status")
    val status: Boolean,
    @SerialName("submission")
    val submission: String,
    @SerialName("task_type")
    val taskType: String,
    @SerialName("title")
    val title: String
)