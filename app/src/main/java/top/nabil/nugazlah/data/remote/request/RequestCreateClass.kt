package top.nabil.nugazlah.data.remote.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestCreateClass(
    @SerialName("description")
    val description: String,
    @SerialName("icon")
    val icon: String,
    @SerialName("lecturer")
    val lecturer: String,
    @SerialName("name")
    val name: String
)