package com.veritas.TMapp.server

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// POST
data class SigninModel(     // 로그인 시 정보를 보낼 때 사용
    val email: String? = null,
    val password: String? = null
)

@Parcelize
data class ResponseSigninModel(   // 로그인 요청 후 응답을 받을 때 사용
    val token: String? = null,
    val uuid: String? = null,
    val username: String? = null
): Parcelable

data class SignupModel( // 회원가입 시 정보를 보낼 때 사용
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val phoneNumber: String? = null,
    val simpleAddress: String? = null,
    val detailAddress: String? = null
)

data class ResponseSignupModel(
    val email: String?
)

@Entity(tableName = "tb_contacts")
data class Contacts(
    @PrimaryKey
    var contactTargetUuid: String,
    var uuid:String,
    var date: String,
    var firstTime: String,
    var lastTime: String?
)