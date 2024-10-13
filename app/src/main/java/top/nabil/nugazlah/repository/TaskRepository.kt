package top.nabil.nugazlah.repository

import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import top.nabil.nugazlah.data.local.Task
import top.nabil.nugazlah.data.local.TaskDao
import top.nabil.nugazlah.data.local.Token
import top.nabil.nugazlah.data.remote.AuthorizedNugazlahApi
import top.nabil.nugazlah.data.remote.request.RequestCreateClass
import top.nabil.nugazlah.data.remote.request.RequestCreateTask
import top.nabil.nugazlah.data.remote.response.ResponseCreateTask
import top.nabil.nugazlah.data.remote.response.ResponseGetDetailTask
import top.nabil.nugazlah.data.remote.response.ResponseGetDetailTaskData
import top.nabil.nugazlah.data.remote.response.ResponseGetMyClassesData
import top.nabil.nugazlah.data.remote.response.ResponseGetMyTasks
import top.nabil.nugazlah.data.remote.response.ResponseGetMyTasksData
import top.nabil.nugazlah.screen.ClassData
import top.nabil.nugazlah.util.ErrorMessage
import top.nabil.nugazlah.util.Resource
import top.nabil.nugazlah.util.parseErrorResponse

class TaskRepository(
    private val authorizedNugazlahApi: AuthorizedNugazlahApi,
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
