package com.example.study_room.Activities.Activities.Adapters

import android.app.Activity
import android.opengl.Visibility
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.study_room.Activities.Activities.Global
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Chat_data
import kotlinx.android.synthetic.main.first_design.view.*
import kotlinx.android.synthetic.main.second_design.view.*

class Chat_Adapter_Student(var Activity : Activity, var data : ArrayList<Chat_data>)
    : RecyclerView.Adapter<Chat_Adapter_Student.ChatViewHolder>() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "chat"
        // var data = ArrayList<Lecture>()
        inner class ChatViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

            var first_tv  = itemView.first_tv
            var second_tv = itemView.second_tv_first_design

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {

            var root = LayoutInflater.from(Activity).inflate(R.layout.first_design, parent,false)
            return ChatViewHolder(root)

//        else{
//            var root = LayoutInflater.from(Activity).inflate(R.layout.second_design, parent,false)
//            return ChatViewHolder(root)
//        }


    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        db = Firebase.firestore

        if (data[position].sender_id == Global.id) {
            Log.e(TAG,"${Global.id} Adapter 11")
            holder.first_tv.visibility =View.VISIBLE
            holder.first_tv.setText(data[position].message)
            holder.second_tv.visibility =View.GONE
        }else{
            Log.e(TAG,"${Global.id} Adapter 12")
            holder.second_tv.visibility =View.VISIBLE
            holder.second_tv.setText(data[position].message)
            holder.first_tv.visibility =View.GONE

        }




    }

    override fun getItemCount(): Int {
        return data.size
    }

}