package org.d3ifcool.nugazyuk.repository

import android.util.Log
import org.d3ifcool.nugazyuk.util.ErrorMessage
import org.d3ifcool.nugazyuk.data.local.Token
import org.d3ifcool.nugazyuk.data.local.TokenDao
import org.d3ifcool.nugazyuk.data.remote.NugazYukApi
import org.d3ifcool.nugazyuk.data.remote.request.LoginRequest
import org.d3ifcool.nugazyuk.data.remote.request.RegisterRequest
import org.d3ifcool.nugazyuk.data.remote.response.LoginResponse
import org.d3ifcool.nugazyuk.data.remote.response.RegisterResponse
import retrofit2.HttpException
import org.d3ifcool.nugazyuk.util.Resource
import org.d3ifcool.nugazyuk.util.parseErrorResponse



class AuthRepository(
    private val nugazlahApi: NugazYukApi,
    private val tokenDao: TokenDao,
) {
    suspend fun getUserId(): Resource<String> {
        return try {
            val token = this.tokenDao.get()
            if (token.id != 0) {
                return Resource.Success(token.userId)
            }
            return Resource.Success("")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Success("")
        }
    }

    suspend fun isLoggedIn(): Resource<Boolean> {
        return try {
            val token = this.tokenDao.get()
            if (token != null) {
                if (token.id != 0) {
                    return Resource.Success(true)
                }
            }
            return Resource.Success(false)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Success(false)
        }
    }

    suspend fun saveToken(token: Token): Resource<String> {
        return try {
            this.tokenDao.insert(token)
            Resource.Success("")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(ErrorMessage.applicationError)
        }
    }

    suspend fun login(request: LoginRequest): Resource<LoginResponse?> {
        return try {
            // Log untuk memastikan data dikirimkan dengan benar
            Log.d("AuthRepository", "Email: ${request.email}, Password: ${request.password}")

            // Kirim permintaan login ke API
            val response = nugazlahApi.login(request)
            Resource.Success(response)
        } catch (e: HttpException) {
            // Log dan penanganan error untuk HttpException
            Log.e("AuthRepository", "HTTP error: ${e.code()} - ${e.message()}")
            e.printStackTrace()
            when (e.code()) {
                400 -> {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = errorBody?.let { json ->
                        parseErrorResponse(json)
                    }
                    if (errorResponse?.code == "C-0005" || errorResponse?.code == "C-0004") {
                        return Resource.Error("Wrong password or email")
                    }
                    return Resource.Error("Error: ${e.message()}")
                }
                500 -> {
                    return Resource.Error(ErrorMessage.applicationError)
                }
                else -> {
                    return Resource.Error(ErrorMessage.applicationError)
                }
            }
        } catch (e: Exception) {
            // Log dan penanganan error umum
            Log.e("AuthRepository", "General error: ${e.message}")
            e.printStackTrace()
            Resource.Error(ErrorMessage.applicationError)
        }
    }

    suspend fun register(request: RegisterRequest): Resource<RegisterResponse?> {
        return try {
            val response = nugazlahApi.register(request)
            Log.d("AuthRepository", "Registration response: $response")
            Resource.Success(response)
        } catch (e: HttpException) {
            Log.e("AuthRepository", "HTTP error: ${e.code()} - ${e.message()}")
            when (e.code()) {
                400 -> {
                    e.printStackTrace()
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = errorBody?.let { json ->
                        parseErrorResponse(json)
                    }
                    if (errorResponse?.code == "C-0005") {
                        return Resource.Error("Please check your input and try again.")
                    }
                    if (errorResponse?.code == "C-0003") {
                        return Resource.Error("The email address is already in use.")
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
            Log.e("AuthRepository", "General error: ${e.message}")
            e.printStackTrace()
            Resource.Error(ErrorMessage.applicationError)
        }
    }


    suspend fun logout(): Resource<String> {
        return try {
            this.tokenDao.delete()
            Resource.Success("")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(ErrorMessage.applicationError)
        }
    }
}
