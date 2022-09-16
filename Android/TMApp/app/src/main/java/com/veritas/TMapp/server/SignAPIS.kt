package com.veritas.TMapp.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.veritas.TMapp.server.ServerSetting.BASE_URL
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

// 사용자 요청을 관리하는 REST API
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
    ): Call<ResponseMsg>

    @PUT("/auth/addFcmToken/")  // FCM Token 갱신 시 요청
    @Headers("accept: application/json", "content-type: application/json")
    fun addFcmToken(
        @Body jsonParams: FcmToken
    ): Call<ResponseMsg>

    @GET("/auth/getRisk/")  // 위험도 정보 받을 때 요청
    @Headers("accept: application/json", "content-type: application/json")
    fun getRisk():Call<Float>

    companion object{
        fun create(): SignAPIS{
            val gson: Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(SignAPIS::class.java)
        }
    }
}