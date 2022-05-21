package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.study_room.R
import kotlinx.android.synthetic.main.activity_lec_or_std.*

class Lec_or_Std : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lec_or_std)

        std_chat.setOnClickListener {
            var i = Intent(this,All_student::class.java)
            startActivity(i)
        }
        lec_chat.setOnClickListener {
            var i = Intent(this,All_teachers::class.java)
            startActivity(i)
        }

    }
}