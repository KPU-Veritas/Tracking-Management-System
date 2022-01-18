package com.example.signinsignup

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface APIS {
    @POST("/auth/signin/")
    @Headers("accept: application/json", "content-type: application/json")
    fun requestLogin(
        @Body jsonparams: UserModel
    ): Call<PostModel>

    companion object{
        private const val BASE_URL = "http://192.168.41.229:8080"

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
