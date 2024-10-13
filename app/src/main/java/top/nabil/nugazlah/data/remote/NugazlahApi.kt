package top.nabil.nugazlah.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import top.nabil.nugazlah.data.remote.request.LoginRequest
import top.nabil.nugazlah.data.remote.request.RegisterRequest
import top.nabil.nugazlah.data.remote.response.LoginResponse
import top.nabil.nugazlah.data.remote.response.RegisterResponse

interface NugazlahApi {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}