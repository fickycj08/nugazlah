package top.nabil.nugazlah.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("user")
    val user: User
)

@Serializable
data class User(
    @SerialName("email")
    val email: String,
    @SerialName("fullname")
    val fullname: String
)