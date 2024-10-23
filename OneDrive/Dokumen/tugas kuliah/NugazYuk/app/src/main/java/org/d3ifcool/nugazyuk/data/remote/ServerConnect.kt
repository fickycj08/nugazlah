package org.d3ifcool.nugazyuk.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object ServerConnect {
    // TODO add interceptor if receipt 401 auto logout
    private const val BASE_URL = "http://10.0.2.2:3000/"
    //private const val BASE_URL = "http://20.205.130.30:80/v1/"
    // private const val BASE_URL = "https://ad7df6561dbf56b0a170215f9234a48a.serveo.net/v1/"

    private var api: NugazYukApi? = null
    private var authorizedApi: AuthorizedNugazYukApi? = null

    fun getInstance(): NugazYukApi {
        if (api == null) {
            setup()
        }
        return api!!
    }

    fun getAuthorizedInstance(authorization: String): AuthorizedNugazYukApi {
        if (authorizedApi == null) {
            setupAuthorized(authorization)
        }
        return authorizedApi!!
    }

    private fun setup() {
        // Menambahkan ignoreUnknownKeys = true agar JSON yang memiliki key tak dikenal bisa diabaikan
        val json = Json {
            ignoreUnknownKeys = true
        }

        val retrofit = Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BASE_URL)
            .build()

        api = retrofit.create(NugazYukApi::class.java)
    }

    private fun setupAuthorized(authorization: String) {
        val httpClient = OkHttpClient.Builder().apply {
            addInterceptor(Interceptor { chain ->
                val original = chain.request()
                val requestWithAuthorization = original.newBuilder()
                    .header("Authorization", "Bearer $authorization")
                    .build()
                chain.proceed(requestWithAuthorization)
            })
        }.build()

        val json = Json {
            ignoreUnknownKeys = true
        }

        val retrofit = Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BASE_URL)
            .client(httpClient)
            .build()

        authorizedApi = retrofit.create(AuthorizedNugazYukApi::class.java)
    }
}
