package com.veritas.TMapp.recyclerview

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.veritas.TMapp.R
import java.io.FileInputStream

class FCMAdapter(val fcmList: ArrayList<FCMs>) : RecyclerView.Adapter<FCMAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_fcm, parent, false)
        return CustomViewHolder(view).apply{
            itemView.setOnClickListener{
                val curPos : Int = adapterPosition
                val fcm : FCMs = fcmList.get(curPos)
                var dlg = AlertDialog.Builder(parent.context)
                dlg.setTitle("${fcm.title}")
                dlg.setMessage("${fcm.body}")
                dlg.setPositiveButton("닫기") { dialogInterface: DialogInterface, i: Int -> }
                dlg.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return fcmList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.img.setImageResource(fcmList.get(position).img)
        holder.title.text = fcmList.get(position).title
        holder.date.text = fcmList.get(position).date
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.iv_fcm)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
    }
}