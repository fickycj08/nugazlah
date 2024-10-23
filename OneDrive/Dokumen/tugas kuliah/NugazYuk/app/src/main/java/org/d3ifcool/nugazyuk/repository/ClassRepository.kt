package org.d3ifcool.nugazyuk.repository

import org.d3ifcool.nugazyuk.data.remote.AuthorizedNugazYukApi
import org.d3ifcool.nugazyuk.data.remote.request.RequestCreateClass
import org.d3ifcool.nugazyuk.data.remote.response.ResponseGetMyClassesData
import org.d3ifcool.nugazyuk.util.ErrorMessage
import retrofit2.HttpException
import org.d3ifcool.nugazyuk.util.Resource
import org.d3ifcool.nugazyuk.util.parseErrorResponse
class ClassRepository(
    private val authorizedNugazlahApi: AuthorizedNugazYukApi,
) {
    suspend fun getMyClasses(): Resource<List<ResponseGetMyClassesData>> {
        return try {
            val response = this.authorizedNugazlahApi.getMyClasses()
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

    suspend fun createClass(classData: RequestCreateClass): Resource<String> {
        return try {
            this.authorizedNugazlahApi.createClass(classData)
            Resource.Success("")
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

    suspend fun joinClass(classCode: String): Resource<String> {
        return try {
            this.authorizedNugazlahApi.joinClass(classCode)
            Resource.Success("")
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> {
                    e.printStackTrace()
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = errorBody?.let { json ->
                        parseErrorResponse(json)
                    }
                    if (errorResponse?.code == "C-0007") {
                        return Resource.Error("User already join the class.")
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
}
