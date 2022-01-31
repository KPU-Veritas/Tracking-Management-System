package com.veritas.datedatabasetest

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class DBController {
    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("yy-MM-dd hh:mm:ss")

    fun setFirstTime(db:AppDatabase, uuid:String){
        val now = System.currentTimeMillis()
        val date = Date(now)
        val firstTime = simpleDateFormat.format(date)

        var contact = db.contactsDao().getContactByUUID(uuid)

        if(contact != null){
            contact.firstTime = firstTime
            db.contactsDao().update(contact)
            Log.d(TAG, "$uuid 처음 시간 업데이트 완료")
        }else{
            contact = Contacts(uuid, firstTime, null)
            db.contactsDao().insertAll(contact)
            Log.d(TAG, "$uuid 처음 시간 생성 완료")
        }
    }

    fun setLastTime(db: AppDatabase, uuid: String){
        val now = System.currentTimeMillis()
        val date = Date(now)
        val lastTime = simpleDateFormat.format(date)

        val contact = db.contactsDao().getContactByUUID(uuid)
        if (contact != null) {
            contact.lastTime = lastTime
            db.contactsDao().update(contact)
            Log.d(TAG, "$uuid 마지막 시간 업데이트 완료")
        }else{
            Log.e(TAG, "처음 시간이 없습니다. 에러")
        }
    }

    fun getAllContacts(db: AppDatabase){
        val savedContacts = db.contactsDao().getAll()
        for(contact in savedContacts){
            Log.d("db_contact","${contact.uuid}  ${contact.firstTime}  ${contact.lastTime}")
        }
    }
    companion object{
        const val TAG = "db_contact"
    }
}