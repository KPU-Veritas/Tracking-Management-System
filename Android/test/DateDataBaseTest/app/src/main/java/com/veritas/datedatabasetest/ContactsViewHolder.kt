package com.veritas.datedatabasetest

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.veritas.datedatabasetest.databinding.ItemContactBinding

class ContactsViewHolder(private val binding: ItemContactBinding, v: View): RecyclerView.ViewHolder(v){
    var view : View = v
    fun bind(item: Contacts) {
        binding.uuid.text = item.uuid
        binding.tvFirstTime.text = item.firstTime
        binding.tvLastTime.text = item.lastTime
    }
}