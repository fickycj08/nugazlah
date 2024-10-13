package top.nabil.nugazlah.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    @SerialName("data")
    val `data`: Data?,
    @SerialName("message")
    val message: String,
    @SerialName("code")
    val code: String
)
