package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.study_room.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_signin_or_signup.*

class Sign_up : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        var i : Intent
        student_sign_up.setOnClickListener {
            i = Intent(this,Student_Sign_up ::class.java)
            startActivity(i)
        }
        lecturer_sign_up.setOnClickListener {
            i = Intent(this,Lecturer_Sign_up ::class.java)
            startActivity(i)
        }
    }
}