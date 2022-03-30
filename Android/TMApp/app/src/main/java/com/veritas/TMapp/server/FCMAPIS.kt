package com.veritas.TMapp.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.veritas.TMapp.server.ServerSetting.token
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

interface FCMAPIS {
    @GET("/fcm/fcmlist/")
    @Headers("accept: application/json", "content-type: application/json")
    fun getFcmList():Call<List<FCMInfo>>

    companion object{
        fun create(): FCMAPIS{
            val gson:Gson = GsonBuilder().setLenient().create()

            val interceptor = AuthenticationInterceptor("Bearer $token")
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(ServerSetting.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build()
                .create(FCMAPIS::class.java)
        }
    }
}