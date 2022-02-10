package com.veritas.TMapp.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.veritas.TMapp.server.ServerSetting.ipv4
import com.veritas.TMapp.server.ServerSetting.portNumber
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SignAPIS {
    @POST("/auth/signin/")  // 로그인 요청
    @Headers("accept: application/json", "content-type: application/json")
    fun requestSignin(
        @Body jsonParams: SigninModel
    ): Call<ResponseSigninModel>

    @POST("/auth/signup/")  // 회원가입 요청
    @Headers("accept: application/json", "content-type: application/json")
    fun requestSignup(
        @Body jsonParams: SignupModel
    ): Call<ResponseSignupModel>


    companion object{
        private const val BASE_URL = "http://$ipv4:$portNumber"

        fun create(): SignAPIS{
            val gson: Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(SignAPIS::class.java)
        }
    }
}