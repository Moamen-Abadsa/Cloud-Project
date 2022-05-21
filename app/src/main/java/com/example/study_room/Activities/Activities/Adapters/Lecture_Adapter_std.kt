package com.example.study_room.Activities.Activities.Adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.study_room.Activities.Activities.All_lectures_for_std
import com.example.study_room.Activities.Activities.View_Lecture
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Lecture
import kotlinx.android.synthetic.main.lecture_design.view.*

class Lecture_Adapter_std(var Activity : Activity, var data : ArrayList<Lecture> )
    :RecyclerView.Adapter<Lecture_Adapter_std.MyViewHolder>() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "lecture"
    inner class MyViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        var name = itemView.textView8
        var desc = itemView.textView10
        var view = itemView.view_lecture
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var root = LayoutInflater.from(Activity).inflate(R.layout.lecture_design, parent, false)
        return MyViewHolder(root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        db = Firebase.firestore

        holder.name.setText(data[position].name)
        holder.desc.setText(data[position].description)
        holder.view.setOnClickListener {
            if (position >= 1){
                    db.collection("student").get().addOnSuccessListener {
                        for (doc in it){
                            var active = doc.get("active").toString().toBoolean()
                            if (active){
                                var id_std = doc.get("mobile_no")
                                db.collection(myCollection).get().addOnSuccessListener {
                                for (doc in it){
                                    var student_view = doc.get("student_view") as ArrayList<String>
                                    for (l in data[position-1].student_view){
                                        if (l == id_std){
                                            holder.view.isEnabled = true
                                            var i = Intent(Activity,View_Lecture::class.java)
                                            i.putExtra("id",data[position].Lecture_id)
                                            Activity.startActivity(i)
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }else{
                holder.view.isEnabled = true
                var i = Intent(Activity,View_Lecture::class.java)
                i.putExtra("id",data[position].Lecture_id)
                Activity.startActivity(i)
            }

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}