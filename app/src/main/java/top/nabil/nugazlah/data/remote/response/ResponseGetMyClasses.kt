package top.nabil.nugazlah.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGetMyClasses(
    @SerialName("code")
    val code: String,
    @SerialName("data")
    val `data`: List<ResponseGetMyClassesData>,
    @SerialName("message")
    val message: String
)