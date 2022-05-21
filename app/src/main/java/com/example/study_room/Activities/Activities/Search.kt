package com.example.study_room.Activities.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.study_room.Activities.Activities.Adapters.Course_Adapter
import com.example.study_room.Activities.Activities.Adapters.Course_Student_Adapter
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Course
import data.Lecture
import kotlinx.android.synthetic.main.activity_all_courses_for_lecturer.*
import kotlinx.android.synthetic.main.activity_search.*

class Search : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    //var myCollection = "course"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        db = Firebase.firestore

        rv_search.layoutManager = LinearLayoutManager(this)

        var i = intent
        var type = i.getStringExtra("Type").toString()
        var name_input = i.getStringExtra("input").toString()

        if (type == "lectures_lec"){
            var data = ArrayList<Lecture>()

        }else if (type == "lectures_std"){
            var data = ArrayList<Lecture>()

        }else if (type == "Course_lecturer"){
            var data = ArrayList<Course>()

            db.collection("course").get().addOnFailureListener {  }.addOnSuccessListener {
                for (document in it) {
                    var name = document.get("name_of_course").toString()
                    var isHide = document.get("isHide").toString().toBoolean()
                    var id = document.get("id").toString()
                    var desc = document.get("desc_of_course").toString()
                    var icon = document.get("icon").toString()

                    var course = Course(name,desc,icon,isHide,id)
                    if (name == name_input){
                        data.add(course)

                    }
//                    if (isHide == false){
//
//                    }

                }
                var adapter = Course_Adapter(this, data)
                rv_search.adapter = adapter

            }


        }else if (type == "Course_student"){
            var data = ArrayList<Course>()

            db.collection("course").get().addOnFailureListener {  }.addOnSuccessListener {
                for (document in it) {
                    var name = document.get("name_of_course").toString()
                    var isHide = document.get("isHide").toString().toBoolean()
                    var id = document.get("id").toString()
                    var desc = document.get("desc_of_course").toString()
                    var icon = document.get("icon").toString()

                    var course = Course(name,desc,icon,isHide,id)
                    if (name == name_input){
                        data.add(course)

                    }

//                    if (isHide == false){
//
//                    }
                }
                var adapter = Course_Student_Adapter(this, data)
                rv_search.adapter = adapter

            }


        }

    }
}