package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Student
import kotlinx.android.synthetic.main.activity_student_sign_in.*
import kotlinx.android.synthetic.main.activity_users_dashboard.*

class Users_Dashboard : AppCompatActivity() {
    //هذا لعرض كل الكورسات عند الطالب
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "student"

    var data = ArrayList<Student>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_dashboard)
        db = Firebase.firestore

        db.collection(myCollection)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    var id_fireStore = document.get("mobile_no").toString()
                    //var id = document.id
                    //var active = document.get("active").toString().toBoolean()
                    var i = intent
                    var mb = i.getStringExtra("mobile_number").toString()
                    if (mb == id_fireStore){
                        Global.id = mb
                        var mobile_no = document.get("mobile_no").toString()
                        var first = document.get("first").toString()
                        var middle = document.get("middle").toString()
                        var last = document.get("last").toString()
                        var email = document.get("email").toString()
                        var address = document.get("address").toString()
                        var birth = document.get("birth_date").toString()
                        var name = "$first $middle $last"

                        student_name_dash.setText(name).toString()
                        student_mobile_dash.setText(mobile_no).toString()
                        student_email_dash.setText(email).toString()
                        student_address_dash.setText(address).toString()
                        student_birthdate_dash.setText(birth).toString()
                    }

                }

            }.addOnFailureListener {}

        ac.setOnClickListener {
            var i = Intent(this,All_courses_for_student::class.java)
            startActivity(i)
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

        }else if(item.itemId == R.id.all_lecturer){
            i = Intent(this,All_teachers::class.java)

        }else if (item.itemId == R.id.all_student){
            i = Intent(this,All_student::class.java)

        }
        startActivity(i)
        return super.onOptionsItemSelected(item)

    }
    }
