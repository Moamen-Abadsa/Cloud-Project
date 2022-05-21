package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class Global() : AppCompatActivity() {

    var i = intent
    var idd = i.getStringExtra("mobile_number").toString()

    companion object{
        var id = ""
    }

}