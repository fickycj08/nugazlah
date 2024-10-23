package org.d3ifcool.nugazyuk.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGetMyTasksData(
    @SerialName("id")
    val id: String,
    @SerialName("deadline")
    val deadline: String,
    @SerialName("description")
    val description: String,
    @SerialName("is_done")
    val isDone: Boolean,
    @SerialName("task_type")
    val taskType: String,
    @SerialName("title")
    val title: String
)