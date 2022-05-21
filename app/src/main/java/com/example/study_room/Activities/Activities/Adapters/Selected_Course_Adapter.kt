package com.example.study_room.Activities.Activities.Adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.study_room.Activities.Activities.All_lectures_for_std
import com.example.study_room.Activities.Activities.Registered_courses
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import data.Course
import data.Lecture
import kotlinx.android.synthetic.main.course_design_student.view.*

class Selected_Course_Adapter (var Activity : Activity, var data : ArrayList<Course>)
    : RecyclerView.Adapter<Selected_Course_Adapter.selectedViewHolder>(){
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "course"
    var selected_courses = ArrayList<String>()

    inner class selectedViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var name = itemView.name_course_student
        var desc = itemView.desc_course_student
        var checked = itemView.is_selected
        var image = itemView.icon_course_student
        var view_btn = itemView.view_course_student
        var id = itemView.id_course_student
        var select = itemView.select
        var cancel = itemView.cancel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): selectedViewHolder {
        var root = LayoutInflater.from(Activity).inflate(R.layout.course_design_student, parent, false)
        return selectedViewHolder(root)
    }

    override fun onBindViewHolder(holder: selectedViewHolder, position: Int) {

        db = Firebase.firestore

        holder.name.setText(data[position].name).toString()
        holder.desc.setText(data[position].description).toString()
        holder.id.setText(data[position].id).toString()

        holder.view_btn.setOnClickListener {
            var i = Intent(Activity, All_lectures_for_std::class.java)
            i.putExtra("course_id",data[position].id)
            Activity.startActivity(i)

        }
        db.collection("student").get().addOnFailureListener {  }
            .addOnSuccessListener {
                for (doc in it){

                    var active = doc.get("active").toString().toBoolean()
                    if (active){

                        var selected = doc.get("selected_courses") as ArrayList<String>

                        if (selected.size >=5){
                            holder.select.isEnabled = false
                        }
                    } }
            }
        holder.select.setOnClickListener {
            var i = Intent(Activity, Registered_courses::class.java)
            i.putExtra("type","reg")
            i.putExtra("id",data[position].id)
            Activity.startActivity(i)
        }

        db.collection("student").get()
            .addOnFailureListener {  }
            .addOnSuccessListener {
                for (doc in it){

                    var active = doc.get("active").toString().toBoolean()
                    if (active){

                        var selected = doc.get("selected_courses") as ArrayList<String>
                        // element == id_corses for the student registered
                        for (element in selected){
                            if (data[position].id == element){
                                holder.checked.isChecked = true
                                holder.select.isEnabled = false
                                holder.cancel.isEnabled = true

                            }else if (data[position].id != element){
                                holder.cancel.isEnabled = false
                            } } } } }

        holder.cancel.setOnClickListener {
            var i = Intent(Activity, Registered_courses::class.java)
            i.putExtra("type","un_reg")
            i.putExtra("id",data[position].id)
            Activity.startActivity(i)
        }
        Picasso.get().load("${data[position].icon}").into(holder.image)

    }

    override fun getItemCount(): Int {
    return data.size
    }
}