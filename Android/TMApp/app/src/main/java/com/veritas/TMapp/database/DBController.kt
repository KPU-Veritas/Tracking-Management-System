package com.veritas.TMapp.database

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.veritas.TMapp.server.Contacts
import com.veritas.TMapp.server.ServerSetting.contactAPIS
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


    fun recordTime(db:AppDatabase, contact_target_uuid:String){
        val runnable = Runnable{
            val now = System.currentTimeMillis()
            val date = Date(now)
            val currentDate = dateFormat.format(date)
            val currentTime = timeFormat.format(date)

            try {
                var contact = db.contactsDao().getContactByUUID(formatingUUID(contact_target_uuid))

                if(contact == null){
                    contact = Contacts(formatingUUID(contact_target_uuid), processedUuid, currentDate, currentTime, null, 0)
                    db.contactsDao().insertAll(contact)
                    Log.d(TAG, "$contact 처음 접촉 시간 등록")
                }
                else{
                    val contactTime = getContactTime(contact.lastTime, currentTime)
                    contact.lastTime = currentTime
                    if (contactTime < 10){
                        contact.contactTime = contact.contactTime + contactTime
                    }
                    db.contactsDao().update(contact)
                    Log.d(TAG, "$contact 마지막 접촉 시간 업데이트")
                }
            }catch (e:SQLiteConstraintException){
                Log.d("SQLiteConstraintException", e.message.toString())
            }
        }
        val thread = Thread(runnable)
        thread.start()
    }

    fun sendAllContacts(db: AppDatabase){
        val runnable = Runnable {
            try{
                val savedContacts = db.contactsDao().getAll()
                for(contact in savedContacts){
                    contactAPIS.postContact(contact).enqueue(object: Callback<String>{
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if(response.code() == 200){
                                Log.d(TAG, "${contact.contactTargetUuid} 전송 성공")
                            }
                            else{
                                Log.d(TAG, "${contact.contactTargetUuid} 전송 실패")
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.d("ContactPost", t.message.toString())
                        }
                    })
                }
                Thread.sleep(1000)
                db.contactsDao().deleteAll()
            }catch (e:SQLiteConstraintException){
                Log.d("SQLiteConstraintException", e.message.toString())
            }
        }
        val thread = Thread(runnable)
        thread.start()
    }
    companion object{
        const val TAG = "db_contact"
    }

    private fun formatingUUID(uuid: String) : String{
        return uuid.substring(0,8) + uuid.substring(9,13) + uuid.substring(14,18) + uuid.substring(19,23) + uuid.substring(24)
    }
    private fun getContactTime(prevTime: String?, currentTime: String): Int{
        if (prevTime == null){
            return 0
        }
        val pt = (prevTime.substring(0,2).toInt() * 60 + prevTime.substring(3,5).toInt()) * 60 + prevTime.substring(6).toInt()
        val ct = (currentTime.substring(0,2).toInt() * 60 + currentTime.substring(3,5).toInt()) * 60 + currentTime.substring(6).toInt()
        return ct-pt
    }
}