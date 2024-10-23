package org.d3ifcool.nugazyuk.data.remote

import org.d3ifcool.nugazyuk.data.remote.request.LoginRequest
import org.d3ifcool.nugazyuk.data.remote.request.RegisterRequest
import org.d3ifcool.nugazyuk.data.remote.response.LoginResponse
import org.d3ifcool.nugazyuk.data.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface NugazYukApi {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}