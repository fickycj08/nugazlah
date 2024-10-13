package top.nabil.nugazlah.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGetMyClassesData(
    @SerialName("description")
    val description: String,
    @SerialName("icon")
    val icon: String,
    @SerialName("id")
    val id: String,
    @SerialName("lecturer")
    val lecturer: String,
    @SerialName("name")
    val name: String,
    @SerialName("code")
    val code: String,
    @SerialName("maker")
    val maker: String
)