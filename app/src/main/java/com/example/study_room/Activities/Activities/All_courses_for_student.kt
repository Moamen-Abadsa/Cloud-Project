package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.study_room.Activities.Activities.Adapters.Course_Adapter
import com.example.study_room.Activities.Activities.Adapters.Course_Student_Adapter
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Course
import kotlinx.android.synthetic.main.activity_all_courses_for_lecturer.*
import kotlinx.android.synthetic.main.activity_all_courses_for_student.*

class All_courses_for_student : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "course"
    var data = ArrayList<Course>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_courses_for_student)
        db = Firebase.firestore

        rv_courses_student.layoutManager = LinearLayoutManager(this)

        db.collection(myCollection).get().addOnFailureListener {  }.addOnSuccessListener {
            for (document in it) {
                var name = document.get("name_of_course").toString()
                var isHide = document.get("isHide").toString().toBoolean()
                var id = document.get("id").toString()
                var desc = document.get("desc_of_course").toString()
                var icon = document.get("icon").toString()

                var course = Course(name,desc,icon,isHide,id)
                if (isHide == false){
                    data.add(course)


                }
                var adapter = Course_Student_Adapter(this, data)
                rv_courses_student.adapter = adapter
            }


        }
        search_btn1.setOnClickListener {
            var input = search_courses_std.text.toString()

            var intent_search = Intent(this,Search::class.java)
            intent_search.putExtra("Type","Course_student")
            intent_search.putExtra("input","$input")
            if (input != null){
                startActivity(intent_search)
            }
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