package top.nabil.nugazlah.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataX(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("user_id")
    val userId: String,
)