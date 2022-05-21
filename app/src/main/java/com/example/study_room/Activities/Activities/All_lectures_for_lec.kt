package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.study_room.Activities.Activities.Adapters.Lecture_Adapter_std
import com.example.study_room.Activities.Activities.Adapters.Lectures_Adapter_lec
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Lecture
import kotlinx.android.synthetic.main.activity_all_courses_for_lecturer.*
import kotlinx.android.synthetic.main.activity_all_courses_for_lecturer.search_courses_lec
import kotlinx.android.synthetic.main.activity_all_lectures_for_lec.*
import kotlinx.android.synthetic.main.activity_all_lectures_for_std.*

class All_lectures_for_lec : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "lecture"
    var data = ArrayList<Lecture>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_lectures_for_lec)
        db = Firebase.firestore
        var i = intent
        var id = i.getStringExtra("course_id").toString()

        search_btn3.setOnClickListener {
            var input = search_lectures_lec.text.toString()

            var intent_search = Intent(this,Search::class.java)
            intent_search.putExtra("Type","lectures_lec")
            intent_search.putExtra("input","$input")
            if (input != null){
                startActivity(intent_search)
            }
        }

        //-------------------------------------------------------------------------------------
        rv_lectures_lec.layoutManager = LinearLayoutManager(this)

        db.collection(myCollection).get().addOnFailureListener {  }.addOnSuccessListener {
            for (document in it) {
                if (document.get("course_id") == id){
                    var name = document.get("name").toString()
                    var isHide = document.get("isHide").toString().toBoolean()
                    var id = document.get("lecture_id").toString()
                    var desc = document.get("desc").toString()
                    var student_view = document.get("student_view") as ArrayList<String>

                    var lecture = Lecture(name,desc,"","","",id,student_view,isHide)
                            data.add(lecture)


                    var adapter = Lectures_Adapter_lec(this, data)
                    rv_lectures_lec.adapter = adapter
                }

            }


        }

    }
}