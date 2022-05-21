package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.study_room.R
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_signin_or_signup.*

class Sign_in : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        var i : Intent
        student_sign_in.setOnClickListener {
            i = Intent(this,Student_Sign_in ::class.java)
            startActivity(i)
        }
        lecturer_sign_in.setOnClickListener {
            i = Intent(this,Lecturer_Sign_in ::class.java)
            startActivity(i)
        }
    }
}