package com.veritas.TMapp.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.veritas.TMapp.server.ServerSetting.BASE_URL
import com.veritas.TMapp.server.ServerSetting.token
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// 접촉 기록을 전송할 때 사용하는 REST API
interface ContactAPIS {
    @POST("/contact/recordcontact/")  // 접촉정보 전송
    @Headers("accept: application/json", "content-type: application/json")
    fun postContact(
        @Body jsonParams: Contacts
    ): Call<String>

    companion object{
        fun create(): ContactAPIS{
            val gson: Gson = GsonBuilder().setLenient().create()

            // 사용자 인증방식에서 JWT를 사용하기 떄문에 token 값 헤더에 추가
            val interceptor = AuthenticationInterceptor("Bearer $token")
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build()
                .create(ContactAPIS::class.java)
        }
    }
}