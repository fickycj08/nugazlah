package org.d3ifcool.nugazyuk.repository

import org.d3ifcool.nugazyuk.data.local.Task
import org.d3ifcool.nugazyuk.data.local.TaskDao
import org.d3ifcool.nugazyuk.data.remote.AuthorizedNugazYukApi
import org.d3ifcool.nugazyuk.data.remote.request.RequestCreateTask
import org.d3ifcool.nugazyuk.data.remote.response.ResponseGetDetailTaskData
import org.d3ifcool.nugazyuk.data.remote.response.ResponseGetMyTasksData
import org.d3ifcool.nugazyuk.util.ErrorMessage
import org.d3ifcool.nugazyuk.util.Resource
import org.d3ifcool.nugazyuk.util.parseErrorResponse
import retrofit2.HttpException

class TaskRepository(
    private val authorizedNugazlahApi: AuthorizedNugazYukApi,
    private val taskDao: TaskDao
) {
    suspend fun getTaskDetail(taskID: String): Resource<ResponseGetDetailTaskData> {
        return try {
            val response = this.authorizedNugazlahApi.getDetailTask(taskID)
            Resource.Success(response.data)
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> {
                    e.printStackTrace()
                    Resource.Error(e.message())
                }

                500 -> {
                    e.printStackTrace()
                    Resource.Error(ErrorMessage.applicationError)
                }

                else -> {
                    e.printStackTrace()
                    Resource.Error(ErrorMessage.applicationError)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(ErrorMessage.applicationError)
        }
    }

    suspend fun getMyTasks(classID: String): Resource<List<ResponseGetMyTasksData>> {
        return try {
            val response = this.authorizedNugazlahApi.getMyTasks(classID)
            Resource.Success(response.data)
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> {
                    e.printStackTrace()
                    Resource.Error(e.message())
                }

                500 -> {
                    e.printStackTrace()
                    Resource.Error(ErrorMessage.applicationError)
                }

                else -> {
                    e.printStackTrace()
                    Resource.Error(ErrorMessage.applicationError)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(ErrorMessage.applicationError)
        }
    }

    suspend fun markTaskDone(taskID: String): Resource<String> {
        return try {
            this.authorizedNugazlahApi.markTaskDone(taskID)
            Resource.Success("")
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> {
                    e.printStackTrace()
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = errorBody?.let { json ->
                        parseErrorResponse(json)
                    }
                    if (errorResponse?.code == "C-0004") {
                        return Resource.Error("Task not found.")
                    }
                    Resource.Error(e.message())
                }

                500 -> {
                    e.printStackTrace()
                    Resource.Error(ErrorMessage.applicationError)
                }

                else -> {
                    e.printStackTrace()
                    Resource.Error(ErrorMessage.applicationError)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(ErrorMessage.applicationError)
        }
    }

    suspend fun createTask(request: RequestCreateTask): Resource<String> {
        return try {
            this.authorizedNugazlahApi.createTask(request)
            Resource.Success("")
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> {
                    e.printStackTrace()
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = errorBody?.let { json ->
                        parseErrorResponse(json)
                    }
                    if (errorResponse?.code == "C-0004") {
                        return Resource.Error("Class not found.")
                    }
                    Resource.Error(e.message())
                }

                500 -> {
                    e.printStackTrace()
                    Resource.Error(ErrorMessage.applicationError)
                }

                else -> {
                    e.printStackTrace()
                    Resource.Error(ErrorMessage.applicationError)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(ErrorMessage.applicationError)
        }
    }

    suspend fun getRegisteredTaskAlarm(classId: String): List<Task> {
        return this.taskDao.getByClassId(classId)
    }

    suspend fun insertTasksToLocal(tasks: List<Task>): Resource<String> {
        return try {
            this.taskDao.batchInsert(tasks)
            Resource.Success("")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(ErrorMessage.applicationError)
        }
    }
}