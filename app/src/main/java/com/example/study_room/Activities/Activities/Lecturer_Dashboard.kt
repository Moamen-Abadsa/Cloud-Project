package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_lecturer_dashboard.*
import kotlinx.android.synthetic.main.activity_users_dashboard.*

class Lecturer_Dashboard : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "lecturer"

  //  lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecturer_dashboard)

      //  auth =Firebase.auth

        var i = intent
        var Current_user_id = i.getStringExtra("mobile_number").toString()

        Global.id = Current_user_id

        db = Firebase.firestore

        db.collection(myCollection)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    var id = document.id
                    var lec_id = Global.id
                    var id_fireStore = document.get("mobile_no").toString()

                   // var active = document.get("active").toString().toBoolean()
                    if (Global.id == id_fireStore){
                        Global.id = id_fireStore
                        var mobile_no = document.get("mobile_no").toString()
                        var first = document.get("first").toString()
                        var middle = document.get("middle").toString()
                        var last = document.get("last").toString()
                        var email = document.get("email").toString()
                        var address = document.get("address").toString()
                        var birth = document.get("birth_date").toString()
                        var name = "$first $middle $last"

                        lecturer_name_dash.setText(name).toString()
                        lecturer_mobil_dash.setText(mobile_no).toString()
                        lecturer_email_dash.setText(email).toString()
                        lecturer_address_dash.setText(address).toString()
                        lecturer_birth_dash.setText(birth).toString()
                    }

                }

            }.addOnFailureListener {

            }
        btn_add_course_dash.setOnClickListener {
          var  i = Intent(this, Add_Course::class.java)
            startActivity(i)

        }
//            btn_add_course.setOnClickListener {
//           var i = Intent(this, Add_Course::class.java)
//            startActivity(i)
        button.setOnClickListener {
            var i = Intent(this,All_courses_for_lecturer::class.java)
            startActivity(i)
        }
        btn_chat.setOnClickListener {
            var i = Intent(this,Lec_or_Std::class.java)
            startActivity(i)
        }
//        }
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

        }else if (item.itemId == R.id.Add_menu_item){
            i = Intent(this,Add_Course::class.java)

        }else if (item.itemId == R.id.all_courses_lec_menu_item){
            i = Intent(this,All_courses_for_lecturer::class.java)

        }else if (item.itemId == R.id.hide_menu_item){
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
