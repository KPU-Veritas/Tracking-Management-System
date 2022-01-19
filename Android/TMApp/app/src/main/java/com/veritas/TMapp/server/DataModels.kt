package com.veritas.TMapp.server

// POST
data class SigninModel(     // 로그인 시 정보를 보낼 때 사용
    val email: String? = null,
    val password: String? = null
)

data class responseSigninModel(   // 로그인 요청 후 응답을 받을 때 사용
    val token: String? = null,
    val uuid: String? = null,
    val email: String? = null
)

data class SignupModel( // 회원가입 시 정보를 보낼 때 사용
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val phoneNumber: String? = null,
    val simpleAddress: String? = null,
    val detailAddress: String? = null
)

data class responseSignupModel(
    val email: String?
)