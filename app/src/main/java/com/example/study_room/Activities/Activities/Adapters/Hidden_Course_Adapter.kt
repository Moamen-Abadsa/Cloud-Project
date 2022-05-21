package com.example.study_room.Activities.Activities.Adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.study_room.Activities.Activities.All_courses_for_lecturer
import com.example.study_room.Activities.Activities.Hidden_courses
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import data.Course
import kotlinx.android.synthetic.main.course_design.view.*
import kotlinx.android.synthetic.main.hidden_course.view.*

class Hidden_Course_Adapter(var Activity: Activity, var data: ArrayList<Course>) :
    RecyclerView.Adapter<Hidden_Course_Adapter.HiddenCourseViewHolder>(){

    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "course"

    inner class HiddenCourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.hidden_name
        var desc = itemView.hidden_desc
        var image = itemView.hidden_image
        var un_hide = itemView.un_hide
        var hidden_id = itemView.hidden_id


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiddenCourseViewHolder {
        var root = LayoutInflater.from(Activity).inflate(R.layout.hidden_course, parent, false)
        return HiddenCourseViewHolder(root)
    }

    override fun onBindViewHolder(holder: HiddenCourseViewHolder, position: Int) {
        db = Firebase.firestore

        holder.name.setText(data[position].name)
        holder.desc.setText(data[position].description)
        holder.hidden_id.setText(data[position].id)

        Picasso.get().load("${data[position].icon}").into(holder.image)

        holder.un_hide.setOnClickListener {
            db.collection(myCollection).get().addOnFailureListener {  }.addOnSuccessListener {
                for (doc in it){
                    if (data[position].id == doc.get("id").toString()){
                        var id = doc.id

                        db.collection(myCollection).document(id).update("isHide" , false).addOnSuccessListener {
                            var i = Intent(Activity, Hidden_courses::class.java)
                            i.putExtra("id_hide", id)
                            Activity.startActivity(i)

                        }.addOnFailureListener {
                            Toast.makeText(Activity,"The hiding proccess is failed", Toast.LENGTH_LONG).show()

                        }
                    }
                }
            }
        }



    }

    override fun getItemCount(): Int {
        return data.size
    }
}