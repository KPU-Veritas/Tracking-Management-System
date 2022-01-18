package com.example.signinsignup

data class UserModel(
    val email: String? = null,
    val password: String? = null
)

data class PostModel(
    val token: String? =null,
    val uuid: String? = null,
    val email: String? = null
)

data class PostResult(
    var result:String? = null
)