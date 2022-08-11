package com.veritas.TMapp.server
// 서버와의 연결 설정 정보를 저장하는 전역 변수
object ServerSetting {
    //const val BASE_URL = "http://PROD-TMS-BACKEND.ap-northeast-2.elasticbeanstalk.com"
    const val BASE_URL = "http://192.168.219.100:8080"
    var processedUuid : String? = null
    lateinit var token: String
    lateinit var username: String
    lateinit var fcmToken: String
    lateinit var contactAPIS:ContactAPIS
    lateinit var fcmAPIS: FCMAPIS
    lateinit var infectAPIS: InfectAPIS
    val signApi = SignAPIS.create()

    fun setUserInfo(uuid: String?, token: String?, username: String?){
        this.processedUuid = uuid!!
        this.token = token!!
        this.username = username!!
        this.contactAPIS = ContactAPIS.create()
        this.fcmAPIS = FCMAPIS.create()
        this.infectAPIS = InfectAPIS.create()
    }

    fun formatUUIDForBeacon(uuid: String) : String{
        return "${uuid.substring(0,8)}-${uuid.substring(8,12)}-${uuid.substring(12,16)}-${uuid.substring(16,20)}-${uuid.substring(20)}"
    }
}