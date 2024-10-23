package org.d3ifcool.nugazyuk.data.remote

import org.d3ifcool.nugazyuk.data.remote.request.RequestCreateClass
import org.d3ifcool.nugazyuk.data.remote.request.RequestCreateTask
import org.d3ifcool.nugazyuk.data.remote.response.ResponseCreateClass
import org.d3ifcool.nugazyuk.data.remote.response.ResponseCreateTask
import org.d3ifcool.nugazyuk.data.remote.response.ResponseGetDetailTask
import org.d3ifcool.nugazyuk.data.remote.response.ResponseGetMyClasses
import org.d3ifcool.nugazyuk.data.remote.response.ResponseGetMyTasks
import org.d3ifcool.nugazyuk.data.remote.response.ResponseJoinClass
import org.d3ifcool.nugazyuk.data.remote.response.ResponseMarkTaskDone
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthorizedNugazYukApi {
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