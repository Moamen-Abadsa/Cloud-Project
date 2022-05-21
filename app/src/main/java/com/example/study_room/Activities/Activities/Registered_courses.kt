package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Course

class Registered_courses : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "student"
    var data = ArrayList<Course>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registered_courses)
        db = Firebase.firestore

        var i = intent
        var x = i.getStringExtra("type").toString()
        var value = i.getStringExtra("id").toString()

        if (x == "reg"){
            db.collection(myCollection).get().addOnFailureListener {  }.addOnSuccessListener {
                for (doc in it){
                    var id = doc.get("mobile_no").toString()
                    if (id == Global.id){
                        var selected = doc.get("selected_courses") as ArrayList<String>
                        var id = doc.id
                       // for (element in selected){
                        if (selected.size  < 5){
                           // for (e in selected){
                              //  if (e != value){
                                    selected.add(value)
                                //}
                           // }

                        }
                       // }
                       // var i = intent
                       // var reg_course = i.getStringExtra("id").toString()
                      //  selected.add(reg_course)


                        db.collection("student").document(id).update("selected_courses",selected).addOnSuccessListener {
                            var i = Intent(this,All_courses_for_student::class.java)
                            startActivity(i)

                            //-----------------------------------------------------------------------------------------------------------------




                            //-----------------------------------------------------------------------------------------------------------------


                        }.addOnFailureListener {  }
                    }

                }
            }
        }else if (x == "un_reg"){
            db.collection(myCollection).get().addOnFailureListener {  }.addOnSuccessListener {
                var courses = ArrayList<String>()
                for (doc in it){
                    var id = doc.get("mobile_no").toString()
                    if (id == Global.id){
//                        var i = intent
//                        var reg_course = i.getStringExtra("id").toString()

                        var selected = doc.get("selected_courses") as ArrayList<String>
                        var id = doc.id
                        for (element in selected){
                            if (element == value){
                                selected.remove(element)
                            }
                           // courses.add(element)

                        }

                        db.collection("student").document(id).update("selected_courses",selected)
                            .addOnFailureListener {  }
                            .addOnSuccessListener {
                                var i = Intent(this,All_courses_for_student::class.java)
                                startActivity(i)


                                //-----------------------------------------------------------------------------------------------------------------




                                //-----------------------------------------------------------------------------------------------------------------


                            }
                    }

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
        if (item.itemId == R.id.home){
            i = Intent(this,Users_Dashboard::class.java)
        }else if (item.itemId == R.id.all_courses_student){
            i = Intent(this,Users_Dashboard::class.java)

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