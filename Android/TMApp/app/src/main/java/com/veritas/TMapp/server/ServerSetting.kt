package com.veritas.TMapp.server

object ServerSetting {
    const val ipv4 = "192.168.42.176"
    const val portNumber = "8080"
    var uuid: String? = null

    fun setUUID(uuid:String){
        this.uuid = uuid
    }
}