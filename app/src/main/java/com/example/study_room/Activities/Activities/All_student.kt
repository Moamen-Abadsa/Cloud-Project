package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.study_room.Activities.Activities.Adapters.Course_Adapter
import com.example.study_room.Activities.Activities.Adapters.Student_Adapter
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Course
import data.Student
import kotlinx.android.synthetic.main.activity_all_courses_for_lecturer.*
import kotlinx.android.synthetic.main.activity_all_student.*

class All_student : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "student"
    var data = ArrayList<Student>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_student)
        db = Firebase.firestore
        rv_student.layoutManager = LinearLayoutManager(this)

        db.collection(myCollection).get().addOnFailureListener {  }.addOnSuccessListener {
            for (document in it) {
                var first = document.get("first").toString()
                var middle = document.get("middle").toString()
                var last = document.get("last").toString()
                var name = "$first $middle $last"
                var mobile = document.get("mobile_no").toString()
                var id = document.id

                var student = Student(first,middle,last,"","","",mobile,"",false)
                    data.add(student)



            }
            var adapter = Student_Adapter(this, data)
            rv_student.adapter = adapter

        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var i = Intent()
        if (item.itemId == R.id.home_student){
            i = Intent(this,Users_Dashboard::class.java)
        }else if (item.itemId == R.id.all_courses_student){
            i = Intent(this,All_courses_for_student::class.java)

        }else if (item.itemId == R.id.selected_courses){
            i = Intent(this,Selected_courses::class.java)

        }else if (item.itemId == R.id.all_users_student){
            i = Intent(this,Student_Dashboard::class.java)

        }else if(item.itemId == R.id.all_lecturer){
            i = Intent(this,All_teachers::class.java)

        }else if (item.itemId == R.id.all_student){
            i = Intent(this,All_student::class.java)

        }
        startActivity(i)
        return super.onOptionsItemSelected(item)

    }
    }
