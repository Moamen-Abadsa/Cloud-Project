package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.study_room.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Lecture
import kotlinx.android.synthetic.main.activity_edit_lecture.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Edit_lecture : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "lecture"
    var data = ArrayList<Lecture>()
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_lecture)
        db = Firebase.firestore
        analytics = Firebase.analytics
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)
        edit_btn.setOnClickListener {
            var name  = edit_name_lec.text.toString()
            var desc = edit_desc_lec.text.toString()
            var i = intent
            var id = i.getStringExtra("lecture_id").toString()
            db.collection(myCollection).document(id).
            update("name" , name , "desc" , desc)

            //-----------------------------------------------------------------------------------------------------------------

            analytics.setUserProperty("User_type","Teacher")
            analytics.logEvent("lecture_editting") {
                param("lecture_name", name )
                param("lecture_id", id )
                param("Date_Time",formatted)

            }


            //-----------------------------------------------------------------------------------------------------------------


        }

    }
}