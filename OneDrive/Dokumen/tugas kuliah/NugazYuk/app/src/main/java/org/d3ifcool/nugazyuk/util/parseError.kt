package org.d3ifcool.nugazyuk.util

import kotlinx.serialization.json.Json
import org.d3ifcool.nugazyuk.data.remote.response.ErrorResponse

fun parseErrorResponse(json: String): ErrorResponse? {
    return try {
        Json.decodeFromString<ErrorResponse>(json)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}