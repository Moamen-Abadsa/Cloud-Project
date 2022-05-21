package com.example.study_room.Activities.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.study_room.Activities.Activities.Adapters.Course_Adapter
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Course
import kotlinx.android.synthetic.main.activity_all_courses_for_lecturer.*

class View_course : AppCompatActivity() {
    //راح نعرض فيها المحاضرات
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "course"
    var data = ArrayList<Course>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_course)
        db = Firebase.firestore


    }
}