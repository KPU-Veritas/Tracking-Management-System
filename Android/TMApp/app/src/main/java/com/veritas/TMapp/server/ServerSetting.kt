package com.veritas.TMapp.server

object ServerSetting {
    const val ipv4 = "192.168.43.30"
    const val portNumber = "8080"
    lateinit var processedUuid : String
    lateinit var token: String
    lateinit var username: String
    lateinit var contactAPIS:ContactAPIS
    val signApi = SignAPIS.create()

    fun setUserInfo(uuid: String, token: String, username: String){
        this.processedUuid = uuid
        this.token = token
        this.username = username
        this.contactAPIS = ContactAPIS.create()
    }

    fun formatUUIDForBeacon(uuid: String) : String{
        return "${uuid.substring(0,8)}-${uuid.substring(8,12)}-${uuid.substring(12,16)}-${uuid.substring(16,20)}-${uuid.substring(20)}"
    }
}