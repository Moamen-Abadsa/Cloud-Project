package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.study_room.Activities.Activities.Adapters.Course_Adapter
import com.example.study_room.Activities.Activities.Adapters.Hidden_Course_Adapter
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Course
import kotlinx.android.synthetic.main.activity_all_courses_for_lecturer.*
import kotlinx.android.synthetic.main.activity_hidden_courses.*

class Hidden_courses : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "course"
    var data = ArrayList<Course>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hidden_courses)
        db = Firebase.firestore

        rv_hidden_courses.layoutManager = LinearLayoutManager(this)

        db.collection(myCollection).get().addOnFailureListener {  }.addOnSuccessListener {
            for (document in it) {
                var name = document.get("name_of_course").toString()
                var isHide = document.get("isHide").toString().toBoolean()
                var id = document.get("id").toString()
                var desc = document.get("desc_of_course").toString()
                var icon = document.get("icon").toString()

                var course = Course(name,desc,icon,isHide,id)
                if (isHide == true){
                    data.add(course)

                }

            }
            var adapter = Hidden_Course_Adapter(this, data)
            rv_hidden_courses.adapter = adapter

        }

    }


    //**********************************************

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu_lecturer,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var i = Intent()
        if (item.itemId == R.id.dashboard_lecturer){
            i = Intent(this,Lecturer_Dashboard::class.java)

        }else if (item.itemId == R.id.all_users_lec_menu_item){
            i = Intent(this,Users_Dashboard::class.java)

        }else if (item.itemId == R.id.Add_menu_item){
            i = Intent(this,Add_Course::class.java)

        }else if (item.itemId == R.id.all_courses_lec_menu_item){
            i = Intent(this,All_courses_for_lecturer::class.java)

        }
        else if (item.itemId == R.id.hide_menu_item){
            i = Intent(this,Hidden_courses::class.java)

        }else if(item.itemId == R.id.all_lec){
            i = Intent(this,All_teachers::class.java)

        }else if (item.itemId == R.id.all_std){
            i = Intent(this,All_student::class.java)

        }
        startActivity(i)
        return super.onOptionsItemSelected(item)

    }

    //**********************************************
}