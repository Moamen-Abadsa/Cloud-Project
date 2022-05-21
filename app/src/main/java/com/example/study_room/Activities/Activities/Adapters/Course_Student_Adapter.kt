package com.example.study_room.Activities.Activities.Adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.realTime_fcm.NotificationData
import com.example.realTime_fcm.PushNotification
import com.example.realTime_fcm.RetrofitInstance
import com.example.study_room.Activities.Activities.All_lectures_for_std
import com.example.study_room.Activities.Activities.Registered_courses
import com.example.study_room.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import data.Course
import kotlinx.android.synthetic.main.activity_add_course.*
import kotlinx.android.synthetic.main.course_design_student.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Course_Student_Adapter(var Activity : Activity , var data : ArrayList<Course>)
    : RecyclerView.Adapter<Course_Student_Adapter.CourseStudentViewHolder>() {

    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "course"
    var selected_courses = ArrayList<String>()
    val topic = "/topics/all"
    private lateinit var analytics: FirebaseAnalytics

    inner class CourseStudentViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        var name = itemView.name_course_student
        var desc = itemView.desc_course_student
        var checked = itemView.is_selected
        var image = itemView.icon_course_student
        var view_btn = itemView.view_course_student
        var id = itemView.id_course_student
        var select = itemView.select
        var cancel = itemView.cancel

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseStudentViewHolder {
        var root = LayoutInflater.from(Activity).inflate(R.layout.course_design_student, parent, false)
        return CourseStudentViewHolder(root)
    }

    override fun onBindViewHolder(holder: CourseStudentViewHolder, position: Int) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)

        db = Firebase.firestore
        analytics = Firebase.analytics

        holder.name.setText(data[position].name).toString()
        holder.desc.setText(data[position].description).toString()
        holder.id.setText(data[position].id).toString()

        holder.view_btn.setOnClickListener {
            var i = Intent(Activity,All_lectures_for_std::class.java)
            i.putExtra("course_id",data[position].id)
            Activity.startActivity(i)

            //-----------------------------------------------------------------------------------------------------------------


            analytics.setUserProperty("User_type","Student")
            analytics.logEvent("View_course") {
                param("course_name", data[position].name)
                param("course_id", data[position].id )
                param("Date_Time",formatted)

            }
            //-----------------------------------------------------------------------------------------------------------------
            FirebaseAnalytics.getInstance(Activity).logEvent("Course_Viewing") {
                param("Course_Name",data[position].name)
            }

            //-----------------------------------------------------------------------------------------------------------------



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
            sendNotification(PushNotification(
                NotificationData(
                "Selected Course",
                "The Course ${data[position].name} has been Selected Successfully at ${formatted}"),
                topic).also { sendNotification(it) })

            analytics.setUserProperty("User_type","Student")
            analytics.logEvent("Register_course") {
                param("course_name", data[position].name)
                param("course_id", data[position].id )
                param("Date_Time",formatted)

            }
            //-----------------------------------------------------------------------------------------------------------------
            FirebaseAnalytics.getInstance(Activity).logEvent("Course_Registration") {
                param("Course_Name",data[position].name)
            }

            //-----------------------------------------------------------------------------------------------------------------


            //-----------------------------------------------------------------------------------------------------------------


            var i = Intent(Activity,Registered_courses::class.java)
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
            sendNotification(PushNotification(
                NotificationData(
                    "Un-Registered Course",
                    "${data[position].name} has been Un-Registered Successfully  at ${formatted}"),
                topic).also { sendNotification(it) })


            //-----------------------------------------------------------------------------------------------------------------


            analytics.setUserProperty("User_type","Student")
            analytics.logEvent("UnRegister_course") {
                param("course_name", data[position].name)
                param("course_id", data[position].id )
                param("Date_Time",formatted)

            }
            //-----------------------------------------------------------------------------------------------------------------
            FirebaseAnalytics.getInstance(Activity).logEvent("Course_UnRegistration") {
                param("Course_Name",data[position].name)
            }

            //-----------------------------------------------------------------------------------------------------------------

            var i = Intent(Activity,Registered_courses::class.java)
            i.putExtra("type","un_reg")
            i.putExtra("id",data[position].id)
            Activity.startActivity(i)
        }
        Picasso.get().load("${data[position].icon}").into(holder.image)
    }

    override fun getItemCount(): Int {
        return data.size
    }
    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("TAG", "Response: ${Gson().toJson(response)}")
            } else {
                Log.e("TAG", response.errorBody()!!.string())
            }
        } catch(e: Exception) {
            Log.e("TAG", e.toString())
        }
    }
}