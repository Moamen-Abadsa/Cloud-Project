package com.example.study_room.Activities.Activities.Adapters

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.study_room.Activities.Activities.All_courses_for_lecturer
import com.example.study_room.Activities.Activities.All_lectures_for_lec
import com.example.study_room.Activities.Activities.Edit_course
import com.example.study_room.Activities.Activities.Edit_lecture
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Lecture
import kotlinx.android.synthetic.main.lecturer_design_for_lecturer.view.*

class Lectures_Adapter_lec (var Activity : Activity , var data : ArrayList<Lecture> )
    : RecyclerView.Adapter<Lectures_Adapter_lec.MyViewHolder>() {

    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "lecture"
    inner class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        var name = itemView.name_lec
        var desc = itemView.desc_lec
        var delete = itemView.delete_lec
        var edit = itemView.edit_lec
        var hide = itemView.hide_lec
        var un_hide = itemView.unhide_lec
        var card = itemView.cardView5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var root = LayoutInflater.from(Activity).inflate(R.layout.lecturer_design_for_lecturer, parent, false)
        return MyViewHolder(root)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        db = Firebase.firestore

        if (data[position].isHide == true){
            holder.card.setBackgroundColor(Color.GRAY)

        }else if (data[position].isHide == false){
            holder.card.setBackgroundColor(Color.WHITE)

        }

        holder.name.setText(data[position].name)
        holder.desc.setText(data[position].description)

        holder.delete.setOnClickListener {
            db.collection(myCollection).get().addOnFailureListener {  }.addOnSuccessListener {
                for (doc in it){

                    if (data[position].Lecture_id == doc.get("lecture_id").toString()){
                        var id = doc.id

                        db.collection(myCollection).document(id).delete().addOnSuccessListener {
                            var i = Intent(Activity, All_lectures_for_lec::class.java)
                            i.putExtra("course_id", data[position].Course_id)
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

                    if (data[position].Lecture_id == doc.get("lecture_id").toString()){
                        var id = doc.id

                        db.collection(myCollection).document(id).update("isHide" , true).addOnSuccessListener {
                            holder.card.setBackgroundColor(Color.GRAY)
                            var i = Intent(Activity, All_lectures_for_lec::class.java)
                            i.putExtra("course_id", data[position].Course_id)
                            Activity.startActivity(i)

                        }.addOnFailureListener {
                            Toast.makeText(Activity,"The hiding proccess is failed", Toast.LENGTH_LONG).show()

                        } } } } }
        holder.edit.setOnClickListener {
            var i = Intent(Activity,Edit_lecture ::class.java)
            i.putExtra("id_lec" , data[position].Lecture_id.toString())
          //  i.putExtra("id_course",data[position].Course_id)
            Activity.startActivity(i)

        }
        holder.un_hide.setOnClickListener {
            db.collection(myCollection).get().addOnFailureListener {  }.addOnSuccessListener {

                for (doc in it){

                    if (data[position].Lecture_id == doc.get("lecture_id").toString()){
                        var id = doc.id

                        db.collection(myCollection).document(id).update("isHide" , false).addOnSuccessListener {
                            holder.card.setBackgroundColor(Color.WHITE)
                            var i = Intent(Activity, All_lectures_for_lec::class.java)
                            i.putExtra("course_id", data[position].Course_id)
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