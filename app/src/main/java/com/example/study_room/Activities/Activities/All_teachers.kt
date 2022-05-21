package com.example.study_room.Activities.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.study_room.Activities.Activities.Adapters.Course_Adapter
import com.example.study_room.Activities.Activities.Adapters.Teacher_Adapter
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Course
import data.Lecturer
import data.Student
import kotlinx.android.synthetic.main.activity_all_courses_for_lecturer.*
import kotlinx.android.synthetic.main.activity_all_teachers.*

class All_teachers : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "lecturer"
    var data = ArrayList<Lecturer>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_teachers)
        db = Firebase.firestore
        rv_teachers.layoutManager = LinearLayoutManager(this)

        db.collection(myCollection).get().addOnFailureListener {  }.addOnSuccessListener {
            for (document in it) {
                var first = document.get("first").toString()
                var middle = document.get("middle").toString()
                var last = document.get("last").toString()
                //var name = "$first $middle $last"
                var mobile = document.get("mobile_no").toString()
                var id = document.id

                var teacher = Lecturer(first,middle,last,"","","",mobile,"",false)
                data.add(teacher)



            }
            var adapter = Teacher_Adapter(this, data)
            rv_teachers.adapter = adapter

        }
    }
}