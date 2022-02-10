package com.veritas.TMapp.server

object ServerSetting {
    const val ipv4 = "192.168.1.69"
    const val portNumber = "8080"
    lateinit var processedUuid : String
    lateinit var token: String
    lateinit var username: String

    fun setUserInfo(uuid: String, token: String, username: String){
        this.processedUuid = formatingUUID(uuid)
        this.token = token
        this.username = username
    }

    private fun formatingUUID(uuid: String) : String{
        return "${uuid.substring(0,8)}-${uuid.substring(8,12)}-${uuid.substring(12,16)}-${uuid.substring(16,20)}-${uuid.substring(20)}"
    }
}