package com.example.study_room.Activities.Activities.Adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.study_room.Activities.Activities.*
import com.example.study_room.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import data.Course
import kotlinx.android.synthetic.main.course_design.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Course_Adapter(var Activity: Activity, var data: ArrayList<Course>) :
    RecyclerView.Adapter<Course_Adapter.CourseViewHolder>() {

    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "course"
    private lateinit var analytics: FirebaseAnalytics
    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.name_of_course
        var desc = itemView.desc_of_course
        var image = itemView.icon_of_course
        var delete = itemView.delete_course
        var hide = itemView.hide_course
        var view = itemView.view_course
        var id = itemView.id_tv
        var add_lec = itemView.btn_add_lecture
        var edit = itemView.edit_course



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        var root = LayoutInflater.from(Activity).inflate(R.layout.course_design, parent, false)
        return CourseViewHolder(root)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        db = Firebase.firestore
        // Obtain the FirebaseAnalytics instance.
        analytics = Firebase.analytics


        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)


        holder.name.setText(data[position].name)
        holder.desc.setText(data[position].description)
        // holder.icon.setImageResource(data[position].icon)
        holder.id.setText(data[position].id)
        Picasso.get().load("${data[position].icon}").into(holder.image);

        holder.delete.setOnClickListener {
            db.collection(myCollection).get().addOnFailureListener {  }.addOnSuccessListener {
                for (doc in it){

                    if (data[position].id == doc.get("id").toString()){
                        var id = doc.id

                        //-----------------------------------------------------------------------------------------------------------------


                        //-----------------------------------------------------------------------------------------------------------------


                        analytics.setUserProperty("User_type","Teacher")
                        analytics.logEvent("course_deleting") {
                            param("course_name", data[position].name)
                            param("course_id", data[position].id )
                            param("Date_Time",formatted)

                        }
                        //-----------------------------------------------------------------------------------------------------------------
                        FirebaseAnalytics.getInstance(Activity).logEvent("Course_deletion") {
                            param("Course_name",data[position].name)
                        }

                        //-----------------------------------------------------------------------------------------------------------------


                        //-----------------------------------------------------------------------------------------------------------------


                        db.collection(myCollection).document(id).delete().addOnSuccessListener {
                            var i = Intent(Activity, All_courses_for_lecturer::class.java)
                            i.putExtra("id_delete", id)
                            Activity.startActivity(i)

                        }.addOnFailureListener {
                            Toast.makeText(Activity,"The deletion proccess is failed",Toast.LENGTH_LONG).show()

                        }
                    }
                }
            }
        }
        holder.hide.setOnClickListener {
            db.collection(myCollection).get().addOnFailureListener {  }.addOnSuccessListener {
                for (doc in it){

                    if (data[position].id == doc.get("id").toString()){
                        var id = doc.id



                        //-----------------------------------------------------------------------------------------------------------------

                        analytics.setUserProperty("User_type","Teacher")
                        analytics.logEvent("course_hiding") {
                            param("course_name", data[position].name)
                            param("course_id", data[position].id )
                            param("Date_Time",formatted)

                        }
                        //-----------------------------------------------------------------------------------------------------------------
                        FirebaseAnalytics.getInstance(Activity).logEvent("Course_Hidding") {
                            param("Course_name",data[position].name)
                        }

                        //-----------------------------------------------------------------------------------------------------------------


                        db.collection(myCollection).document(id).update("isHide" , true).addOnSuccessListener {
                        var i = Intent(Activity, All_courses_for_lecturer::class.java)
                        i.putExtra("id_hide", id)
                        Activity.startActivity(i)

            }.addOnFailureListener {
                Toast.makeText(Activity,"The hiding proccess is failed",Toast.LENGTH_LONG).show()

            }
                    }
                }
            }

        }
        holder.view.setOnClickListener {
            var i = Intent(Activity, All_lectures_for_lec::class.java)
            i.putExtra("course_id", data[position].id)
            Activity.startActivity(i)

        }

        holder.add_lec.setOnClickListener {

            var i = Intent(Activity, Add_Lecture::class.java)
            i.putExtra("id_course", data[position].id.toString())
            Activity.startActivity(i)

        }
        holder.edit.setOnClickListener {
            var i = Intent(Activity, Edit_course::class.java)
            i.putExtra("id", data[position].id)
            i.putExtra("name", data[position].name)
            i.putExtra("desc", data[position].description)
            i.putExtra("icon", data[position].icon)
            i.putExtra("isHide", data[position].isHide)
            Activity.startActivity(i)


        }

    }

        override fun getItemCount(): Int {
            return data.size

        }

}

