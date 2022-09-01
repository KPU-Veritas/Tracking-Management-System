package com.veritas.TMapp.server
// 서버와의 연결 설정 정보를 저장하는 전역 변수
object ServerSetting {
    const val BASE_URL = "http://TMS-SERVER.us-west-2.elasticbeanstalk.com"
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