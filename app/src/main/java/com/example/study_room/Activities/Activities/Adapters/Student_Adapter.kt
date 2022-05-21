package com.example.study_room.Activities.Activities.Adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.study_room.Activities.Activities.Chat_Student
import com.example.study_room.R
import data.Student
import kotlinx.android.synthetic.main.student_design.view.*

class Student_Adapter(var Activity: Activity, var data: ArrayList<Student>): RecyclerView.Adapter<Student_Adapter.StudentViewHolder>() {
    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.name_student
        var mobile = itemView.mobile_student
        var chat_btn = itemView.chat_student


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        var root = LayoutInflater.from(Activity).inflate(R.layout.student_design, parent, false)
        return StudentViewHolder(root)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.name.setText("${data[position].first} ${data[position].middle} ${data[position].last}").toString()
        holder.mobile.setText(data[position].mobile_no).toString()

        holder.chat_btn.setOnClickListener {
            var i = Intent(Activity , Chat_Student::class.java)
            i.putExtra("receiver" , data[position].mobile_no)
            Activity.startActivity(i)
        }

    }

    override fun getItemCount(): Int {
    return data.size
    }
}