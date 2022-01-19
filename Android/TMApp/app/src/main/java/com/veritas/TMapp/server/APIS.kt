package com.veritas.TMapp.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIS {
    @POST("/auth/signin/")  // 로그인 요청
    @Headers("accept: application/json", "content-type: application/json")
    fun requestSingin(
        @Body jsonParams: SigninModel
    ): Call<responseSigninModel>

    @POST("/auth/signup/")  // 회원가입 요청
    @Headers("accept: application/json", "content-type: application/json")
    fun requestSingup(
        @Body jsonParams: SignupModel
    ): Call<responseSignupModel>


    companion object{
        private const val ipv4 = "192.168.41.229"
        private const val portNumber = "8080"
        private const val BASE_URL = "http://$ipv4:$portNumber"

        fun create(): APIS{
            var gson: Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(APIS::class.java)
        }
    }
}