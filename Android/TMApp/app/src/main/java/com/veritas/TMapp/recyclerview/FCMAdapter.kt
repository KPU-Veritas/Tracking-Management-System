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
import com.veritas.TMapp.server.FCMInfo
import java.io.FileInputStream

class FCMAdapter(val fcmList: ArrayList<FCMs>) : RecyclerView.Adapter<FCMAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_fcm, parent, false)
        return CustomViewHolder(view).apply{
            itemView.setOnClickListener{
                val curPos : Int = adapterPosition
                val fcm : FCMs = fcmList.get(curPos)
                var ifs = FileInputStream("/data/user/0/com.veritas.TMapp/files/FCM/${fcm.title}.txt") //sd카드에 저장된 title.txt 파일을 읽기 모드로 연다.
                var txt = ByteArray(ifs.available())
                ifs.read(txt)
                var content = txt.toString(Charsets.UTF_8) //title.txt의 내용을 읽고 스트링으로 변환하여 content에 저장
                ifs.close()
                var dlg = AlertDialog.Builder(parent.context)
                dlg.setTitle("${fcm.title}")
                dlg.setMessage("$content")
                dlg.setPositiveButton("닫기") { dialogInterface: DialogInterface, i: Int -> }
                dlg.show()
            }
        }
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