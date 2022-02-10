package com.veritas.TMapp.database

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.veritas.TMapp.server.ContactAPIS
import com.veritas.TMapp.server.Contacts
import com.veritas.TMapp.server.ServerSetting.processedUuid
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DBController {
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yy-MM-dd")
    @SuppressLint("SimpleDateFormat")
    private val timeFormat = SimpleDateFormat("hh:mm:ss")
    private var contactAPIS = ContactAPIS.create()

    fun recordTime(db:AppDatabase, contact_target_uuid:String){
        val now = System.currentTimeMillis()
        val date = Date(now)
        val currentDate = dateFormat.format(date)
        val currentTime = timeFormat.format(date)

        try {
            var contact = db.contactsDao().getContactByUUID(contact_target_uuid)

            if(contact == null){
                contact = Contacts(contact_target_uuid, processedUuid, currentDate, currentTime, null)
                db.contactsDao().insertAll(contact)
                Log.d(TAG, "$contact_target_uuid 처음 접촉 시간 등록")
            }
            else{
                contact.lastTime = currentTime
                db.contactsDao().update(contact)
                Log.d(TAG, "$contact_target_uuid 마지막 접촉 시간 업데이트")
            }
        }catch (e:SQLiteConstraintException){
            Log.d("SQLiteConstraintException", e.message.toString())
        }
    }

    fun sendAllContacts(db: AppDatabase){
        try{
            val savedContacts = db.contactsDao().getAll()
            for(contact in savedContacts){
                contactAPIS.postContact(contact).enqueue(object: Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(response.code() == 200){
                            Log.d(TAG, "${contact.contact_target_uuid} 전송 성공")
                        }
                        else{
                            Log.d(TAG, "${contact.contact_target_uuid} 전송 실패")
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("ContactPost", t.message.toString())
                    }
                })
            }
            db.contactsDao().deleteAll()
        }catch (e:SQLiteConstraintException){
            Log.d("SQLiteConstraintException", e.message.toString())
        }
    }
    companion object{
        const val TAG = "db_contact"
    }
}