package com.veritas.TMapp.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.veritas.TMapp.server.ServerSetting.BASE_URL
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
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

    companion object{
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