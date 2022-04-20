package com.veritas.TMapp.server

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SigninModel(     // 로그인 시 body 정보 클래스
    val email: String? = null,
    val password: String? = null
)

data class ResponseSigninModel(   // 로그인 시 응답 클래스
    val token: String? = null,
    val uuid: String? = null,
    val username: String? = null
)

data class SignupModel( // 회원가입 시 body 정보 클래스
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val phoneNumber: String? = null,
    val simpleAddress: String? = null,
    val detailAddress: String? = null
)

data class PostInfectModel( // 감염 정보 전송 시 body 정보 클래스
    val uuid: String? = null,
    val judgmentDate: String? = null,
    val estimatedDate: String? = null,
    val detailSituation: String? = null
)

data class FcmToken(    // FCM 토큰 값을 갱신 시 body 정보 클래스
    val uuid: String? = null,
    val fcmToken: String? = null
)

data class ResponseMsg( // 응답 메시지 클래스
    val error: String? = null,
    val data: List<Any>? = null
)

data class FCMInfo( // FCM 알림 수신 시 body 정보 클래스
    val id: Long? = null,
    val uuid: String? = null,
    val date: String? = null,
    val time: String? = null,
    val title: String? = null,
    val body: String? = null,
    val risk: Float? = null,
    val contactDegree: Int? = null
)

class FCMInfoList(
    @SerializedName("data")
    val fcmInfoList: List<FCMInfo>
)

@Entity(tableName = "tb_contacts")  // 접촉 기록을 저장하는 테이블, RoomDB 사용
data class Contacts(
    @PrimaryKey
    var contactTargetUuid: String,
    var uuid:String,
    var date: String,
    var firstTime: String,
    var lastTime: String?,
    var contactTime: Int
)