package com.veritas.TMapp.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.veritas.TMapp.server.ServerSetting.ipv4
import com.veritas.TMapp.server.ServerSetting.portNumber
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ContactAPIS {
    @POST("/contact/")  // 접촉정보 전송
    @Headers("accept: application/json", "content-type: application/json")
    fun postContact(
        @Body jsonParams: Contacts
    ): Call<String>

    companion object{
        private const val BASE_URL = "http://$ipv4:$portNumber"

        fun create(authToken: String): SignAPIS{
            val gson: Gson = GsonBuilder().setLenient().create()

            val interceptor = AuthenticationInterceptor("Bearer $authToken")
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build()
                .create(SignAPIS::class.java)
        }
    }
}