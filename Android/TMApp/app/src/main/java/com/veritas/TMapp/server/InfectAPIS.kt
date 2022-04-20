package com.veritas.TMapp.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface InfectAPIS {
    @POST("/infected/addinfected/")  // 감염 정보 전송
    @Headers("accept: application/json", "content-type: application/json")
    fun postInfect(
        @Body jsonParams: PostInfectModel
    ): Call<ResponseMsg>

    companion object {
        fun create(): InfectAPIS {
            val gson: Gson = GsonBuilder().setLenient().create()

            // 사용자 인증방식에서 JWT를 사용하기 떄문에 token 값 헤더에 추가
            val interceptor = AuthenticationInterceptor("Bearer ${ServerSetting.token}")
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(ServerSetting.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build()
                .create(InfectAPIS::class.java)
        }
    }
}