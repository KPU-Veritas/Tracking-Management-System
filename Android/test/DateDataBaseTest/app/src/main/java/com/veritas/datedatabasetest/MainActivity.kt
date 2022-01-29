package com.veritas.datedatabasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.veritas.datedatabasetest.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val simpleDateFormat = SimpleDateFormat("yy-MM-dd hh:mm:ss")
    val TAG = "MainActivity"
    val uuid = "19980930001023072441000000000000"
    var db: AppDatabase?= null
    var contactsList = mutableListOf<Contacts>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)

        val savedContacts = db!!.contactsDao().getAll()
        if (savedContacts.isNotEmpty()){
            contactsList.addAll(savedContacts)
        }

        val adapter = ContactsListAdapter(contactsList)
        adapter.setItemClickListener(object :
        ContactsListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val contacts = contactsList[position]
                db?.contactsDao()?.delete(contacts = contacts)
                contactsList.removeAt(position)
                adapter.notifyDataSetChanged()

                Log.d(TAG, "remove item($position). name:${contacts.uuid}")
            }
        })
        binding.rvContacts.adapter = adapter

        binding.btnFirstTime.setOnClickListener {
            val now = System.currentTimeMillis()
            val date = Date(now)
            val firstTime = simpleDateFormat.format(date)

            var contact = db?.contactsDao()?.getContactByUUID(uuid)

            if (contact != null){
                contact.firstTime = firstTime
                db?.contactsDao()?.update(contact)
            }
            else{
                contact = Contacts(uuid, firstTime, null)
                db?.contactsDao()?.insertAll(contact)
            }
            contactsList.add(contact)
            adapter.notifyDataSetChanged()

            binding.tvFirstTime.text = firstTime
            allDB()
        }

        binding.btnLastTime.setOnClickListener {
            val now = System.currentTimeMillis()
            val date = Date(now)
            val lastTime = simpleDateFormat.format(date)

            val contact = db?.contactsDao()?.getContactByUUID(uuid)
            if (contact != null){
                contact.lastTime = lastTime
                db?.contactsDao()?.update(contact)
                contactsList.add(contact)
                adapter.notifyDataSetChanged()
            }
            else{
                Log.e(TAG, "처음 시간이 없습니다. 에러")
            }
            binding.tvLastTime.text = simpleDateFormat.format(date)
            allDB()
        }
    }
    fun allDB(){
        db = AppDatabase.getInstance(this)

        val savedContacts = db!!.contactsDao().getAll()
        for(contact in savedContacts){
            Log.d("db_contact","${contact.uuid}  ${contact.firstTime}  ${contact.lastTime}")
        }
    }
}