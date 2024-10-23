package org.d3ifcool.nugazyuk.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGetDetailTask(
    @SerialName("code")
    val code: String,
    @SerialName("data")
    val `data`: ResponseGetDetailTaskData,
    @SerialName("message")
    val message: String
)