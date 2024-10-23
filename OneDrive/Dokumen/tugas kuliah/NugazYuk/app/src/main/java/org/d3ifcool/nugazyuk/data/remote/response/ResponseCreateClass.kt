package org.d3ifcool.nugazyuk.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseCreateClass(
    @SerialName("code")
    val code: String,
    @SerialName("data")
    val data: ResponseGetMyClassesData?,  // Menggunakan model yang sudah ada
    @SerialName("message")
    val message: String
)
