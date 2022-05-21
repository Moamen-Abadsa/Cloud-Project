package com.example.study_room.Activities.Activities.Adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.study_room.Activities.Activities.Chat_Student
import com.example.study_room.Activities.Activities.Chat_Teacher
import com.example.study_room.R
import data.Lecturer
import kotlinx.android.synthetic.main.lecturer_design.view.*

class Teacher_Adapter (var Activity: Activity, var data: ArrayList<Lecturer>)
    : RecyclerView.Adapter<Teacher_Adapter.TeacherViewHolder>() {
    inner class TeacherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.name_lecturer
        var mobile = itemView.mobile_lecturer
        var chat_btn = itemView.chat_lecturer


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        var root = LayoutInflater.from(Activity).inflate(R.layout.lecturer_design, parent, false)
        return TeacherViewHolder(root)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        holder.name.setText("${data[position].first} ${data[position].middle} ${data[position].last}")
        holder.mobile.setText(data[position].mobile_no)
        holder.chat_btn.setOnClickListener {
            var i = Intent(Activity , Chat_Teacher::class.java)
            i.putExtra("receiver" , data[position].mobile_no)
            Activity.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}