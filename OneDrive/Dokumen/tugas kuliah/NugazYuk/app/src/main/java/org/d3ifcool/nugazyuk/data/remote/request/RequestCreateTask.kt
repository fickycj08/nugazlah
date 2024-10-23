package org.d3ifcool.nugazyuk.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestCreateTask(
    @SerialName("class_id")
    val classId: String,
    @SerialName("deadline")
    val deadline: String,
    @SerialName("description")
    val description: String,
    @SerialName("detail")
    val detail: String,
    @SerialName("submission")
    val submission: String,
    @SerialName("task_type")
    val taskType: String,
    @SerialName("title")
    val title: String
)