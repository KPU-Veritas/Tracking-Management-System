package com.veritas.datedatabasetest

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_contacts")
data class Contacts(
    @PrimaryKey var uuid:String,
    var firstTime: String,
    var lastTime: String?
)
