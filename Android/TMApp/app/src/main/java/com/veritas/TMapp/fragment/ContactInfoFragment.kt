package com.veritas.TMapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.veritas.TMapp.databinding.FragmentContactInfoBinding
import android.content.Intent
import com.veritas.TMapp.CheckFCMActivity
import com.veritas.TMapp.SendInfectionInfoActivity

class ContactInfoFragment : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        val binding = FragmentContactInfoBinding.inflate(inflater, container, false)
        binding.btnConfirm.setOnClickListener {
            val intent = Intent(activity, SendInfectionInfoActivity::class.java)
            startActivity(intent)
        }
        binding.btnNotification.setOnClickListener {
            val intent = Intent(activity, CheckFCMActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}