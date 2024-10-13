package top.nabil.nugazlah.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import top.nabil.nugazlah.data.remote.request.LoginRequest
import top.nabil.nugazlah.data.remote.request.RegisterRequest
import top.nabil.nugazlah.data.remote.request.RequestCreateClass
import top.nabil.nugazlah.data.remote.request.RequestCreateTask
import top.nabil.nugazlah.data.remote.response.LoginResponse
import top.nabil.nugazlah.data.remote.response.RegisterResponse
import top.nabil.nugazlah.data.remote.response.ResponseCreateClass
import top.nabil.nugazlah.data.remote.response.ResponseCreateTask
import top.nabil.nugazlah.data.remote.response.ResponseGetDetailTask
import top.nabil.nugazlah.data.remote.response.ResponseGetMyClasses
import top.nabil.nugazlah.data.remote.response.ResponseGetMyTasks
import top.nabil.nugazlah.data.remote.response.ResponseJoinClass
import top.nabil.nugazlah.data.remote.response.ResponseMarkTaskDone

interface AuthorizedNugazlahApi {
    @POST("classes")
    suspend fun createClass(@Body request: RequestCreateClass): ResponseCreateClass

    @GET("classes")
    suspend fun getMyClasses(): ResponseGetMyClasses

    @POST("classes/{classCode}/join")
    suspend fun joinClass(@Path("classCode") classId: String): ResponseJoinClass

    @POST("tasks")
    suspend fun createTask(@Body request: RequestCreateTask): ResponseCreateTask

    @GET("tasks/classes/{classID}")
    suspend fun getMyTasks(@Path("classID") classID: String): ResponseGetMyTasks

    @GET("tasks/{taskID}")
    suspend fun getDetailTask(@Path("taskID") taskID: String): ResponseGetDetailTask

    @POST("tasks/{taskID}/done")
    suspend fun markTaskDone(@Path("taskID") taskID: String): ResponseMarkTaskDone
}