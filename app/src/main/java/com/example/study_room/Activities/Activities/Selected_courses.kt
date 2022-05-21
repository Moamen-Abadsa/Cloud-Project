package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.study_room.Activities.Activities.Adapters.Course_Student_Adapter
import com.example.study_room.Activities.Activities.Adapters.Selected_Course_Adapter
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Course
import kotlinx.android.synthetic.main.activity_all_courses_for_student.*
import kotlinx.android.synthetic.main.activity_all_courses_for_student.rv_courses_student
import kotlinx.android.synthetic.main.activity_selected_courses.*

class Selected_courses : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "courses"
    var data = ArrayList<Course>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_courses)
        db = Firebase.firestore

        rv_selected.layoutManager = LinearLayoutManager(this)

        db.collection("student").get().addOnFailureListener {}.addOnSuccessListener {

            for (doc in it){
                Log.e(TAG,"$TAG 6666")

                if (doc.get("mobile_no").toString() == Global.id){
                    Log.e(TAG,"$TAG 7777")
                    var selected = doc.get("selected_courses") as ArrayList<String>
                    for (course in selected){
                        Log.e(TAG,"$TAG 8888")

                        db.collection("course").get().addOnFailureListener {}.addOnSuccessListener {
                            for (doc in it){
                                Log.e(TAG,"$TAG 9999")

                                var course_id = doc.get("id").toString()
                                if (course_id == course){
                                    Log.e(TAG,"$TAG 1010101010")

                                    var desc_of_course = doc.get("desc_of_course").toString()
                                    var id = doc.get("id").toString()
                                    var name = doc.get("name_of_course").toString()
                                    var isHide = doc.get("isHide").toString().toBoolean()
                                    var icon = doc.get("icon").toString()
                                    data.add(Course(name,desc_of_course,icon,isHide,id))


                                }
                                var adapter = Selected_Course_Adapter(this, data)
                                rv_selected.adapter = adapter
                            }
                        }
                    }
                }

            }
        }
    }




    //----------------------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var i = Intent()
        if (item.itemId == R.id.home){
            i = Intent(this,Student_Dashboard::class.java)
        }else if (item.itemId == R.id.all_courses_student){
            i = Intent(this,Users_Dashboard::class.java)

        }else if (item.itemId == R.id.selected_courses){
            i = Intent(this,Selected_courses::class.java)

        }else if (item.itemId == R.id.all_users_student){
            i = Intent(this,All_Users::class.java)

        }else if(item.itemId == R.id.all_lecturer){
            i = Intent(this,All_teachers::class.java)

        }else if (item.itemId == R.id.all_student){
            i = Intent(this,All_student::class.java)

        }
        startActivity(i)
        return super.onOptionsItemSelected(item)

    }
}