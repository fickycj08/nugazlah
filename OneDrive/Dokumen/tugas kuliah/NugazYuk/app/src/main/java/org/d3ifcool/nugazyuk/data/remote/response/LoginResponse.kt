package org.d3ifcool.nugazyuk.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("data")
    val `data`: DataX?,
    @SerialName("message")
    val message: String,
    @SerialName("code")
    val code: String
)