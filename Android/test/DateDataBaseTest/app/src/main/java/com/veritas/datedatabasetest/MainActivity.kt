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
    private val uuid = "19980930001023072441000000000001"
    var db: AppDatabase?= null
    private var dbController:DBController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)
        dbController = DBController()

        binding.btnFirstTime.setOnClickListener {
            dbController!!.setFirstTime(db!!, uuid)
            dbController!!.getAllContacts(db!!)
        }

        binding.btnLastTime.setOnClickListener {
            dbController!!.setLastTime(db!!, uuid)
            dbController!!.getAllContacts(db!!)
        }
    }
}