package com.veritas.TMapp.server

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthenticationInterceptor(token: String):Interceptor {
    val authToken = token
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request() as Request
        val builder: Request.Builder = original.newBuilder()
            .header("Authorization", authToken)
        val request = builder.build()
        return chain.proceed(request)
    }
}