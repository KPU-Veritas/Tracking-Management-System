package com.veritas.TMapp.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.veritas.TMapp.R

class FCMAdapter(val fcmList : ArrayList<FCMs>) : RecyclerView.Adapter<FCMAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_fcm, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return fcmList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.num.setImageResource(fcmList.get(position).num)
        holder.title.text = fcmList.get(position).title
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val num = itemView.findViewById<ImageView>(R.id.iv_fcm)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
    }
}