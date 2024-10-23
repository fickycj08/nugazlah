package org.d3ifcool.nugazyuk.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    @SerialName("email")
    val email: String,
    @SerialName("fullname")
    val fullname: String,
    @SerialName("password")
    val password: String
)