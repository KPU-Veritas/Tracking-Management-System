package com.veritas.TMapp.server

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

// JWT 인증 방식을 위해 사용하는 클래스
class AuthenticationInterceptor(token: String):Interceptor {
    val authToken = token
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request() as Request
        val builder: Request.Builder = original.newBuilder()
            .header("Authorization", authToken) // token 값을 header에 저장시킨다.
        val request = builder.build()
        return chain.proceed(request)
    }
}