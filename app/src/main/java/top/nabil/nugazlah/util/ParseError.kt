package top.nabil.nugazlah.util

import kotlinx.serialization.json.Json
import top.nabil.nugazlah.data.remote.response.ErrorResponse

fun parseErrorResponse(json: String): ErrorResponse? {
    return try {
        Json.decodeFromString<ErrorResponse>(json)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}