package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.study_room.Activities.Activities.Adapters.Course_Student_Adapter
import com.example.study_room.Activities.Activities.Adapters.Lecture_Adapter_std
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Course
import data.Lecture
import kotlinx.android.synthetic.main.activity_all_courses_for_lecturer.*
import kotlinx.android.synthetic.main.activity_all_courses_for_lecturer.search_courses_lec
import kotlinx.android.synthetic.main.activity_all_courses_for_student.*
import kotlinx.android.synthetic.main.activity_all_lectures_for_std.*

class All_lectures_for_std : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "lecture"
    var data = ArrayList<Lecture>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_lectures_for_std)
        db = Firebase.firestore
        var i = intent
        var Course_id = i.getStringExtra("course_id").toString()
        search_btn4.setOnClickListener {
            var input = search_lectures_std.text.toString()

            var intent_search = Intent(this,Search::class.java)
            intent_search.putExtra("Type","lectures_std")
            intent_search.putExtra("input","$input")
            if (input != null){
                startActivity(intent_search)
            }
        }

        //--------------------------------------------------------------------------------------------
        rv_lectures_std.layoutManager = LinearLayoutManager(this)

        db.collection(myCollection).get().addOnFailureListener {  }.addOnSuccessListener {
            for (document in it) {
                if (document.get("course_id") == Course_id){
                    var name = document.get("name").toString()
                    var isHide = document.get("isHide").toString().toBoolean()
                    var id = document.get("lecture_id").toString()
                    var desc = document.get("desc").toString()
                    var student_view = document.get("student_view") as ArrayList<String>


                    var lecture = Lecture(name,desc,"","","",id,student_view,isHide)
                    if (isHide == false){
                        data.add(lecture)


                    }
                    var adapter = Lecture_Adapter_std(this, data)
                    rv_lectures_std.adapter = adapter
                }

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