package com.veritas.TMapp.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.veritas.TMapp.server.ServerSetting.token
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
// FCM 알림 정보를 받아올 때 사용하는 REST API
interface FCMAPIS {
    @GET("/fcm/fcmlist/")   // FCM List 수신 시  사용
    @Headers("accept: application/json", "content-type: application/json")
    fun getFcmList():Call<FCMInfoList>

    companion object{
        fun create(): FCMAPIS{
            val gson:Gson = GsonBuilder().setLenient().create()

            // 사용자 인증방식에서 JWT를 사용하기 떄문에 token 값 헤더에 추가
            val interceptor = AuthenticationInterceptor("Bearer $token")
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(ServerSetting.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build()
                .create(FCMAPIS::class.java)
        }
    }
}