package com.example.study_room.Activities.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.realTime_fcm.NotificationData
import com.example.realTime_fcm.PushNotification
import com.example.realTime_fcm.RetrofitInstance
import com.example.study_room.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import data.Course
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.android.synthetic.main.activity_add_course.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Add_Course : AppCompatActivity() {
    var fileUri: Uri? = null


    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "course"
    var IMAGE_URI: Uri? = null
    val topic = "/topics/all"

    private lateinit var analytics: FirebaseAnalytics

    private val PICK_IMAGE_REQUEST = 111
    var data2 = ArrayList<Course>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        db = Firebase.firestore

        // Obtain the FirebaseAnalytics instance.
        analytics = Firebase.analytics

        val storage = Firebase.storage
        val StorageRef = storage.reference
        val imageRef = StorageRef.child("icons")



        FirebaseMessaging.getInstance().subscribeToTopic(topic)

        icon_course.setOnClickListener {
            val i = Intent()
            i.action = Intent.ACTION_PICK
            i.type = "image/*"
            startActivityForResult(i, PICK_IMAGE_REQUEST)
        }

        btn_submitt_course.setOnClickListener {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val formatted = current.format(formatter)

            PushNotification(
                NotificationData(
                    "Add Course",
                    "${add_name_course.text.toString()} has been Added Successfully at ${formatted}"),
                topic).also {
                sendNotification(it)
            }

            sendNotification(PushNotification(NotificationData(
                "Add Course",
                "${add_name_course.text.toString()} has been Added Successfully at ${formatted}"),
                topic).also { sendNotification(it) })





            // Get the data from an ImageView as bytes )
            val bitmap = (icon_course.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val data = baos.toByteArray()

            val childRef = imageRef.child(System.currentTimeMillis().toString() + "_course.png")
            var uploadTask = childRef.putBytes(data)
            uploadTask.addOnFailureListener { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_LONG).show()
                // Handle unsuccessful uploads

            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                childRef.downloadUrl.addOnSuccessListener { uri ->
                    Log.e("MSA", uri.toString() + " . 111")
                    fileUri = uri
                    var name_of_course = add_name_course.text.toString()
                    var desc_of_course = add_desc_course.text.toString()
                    //Log.e("MSA", fileUri.toString() + " . 222")

                   var id =  (System.currentTimeMillis()/2).toInt()
                    var course = hashMapOf(
                        "name_of_course" to name_of_course,
                        "desc_of_course" to desc_of_course,
                        "isHide" to false,
                        "id" to "${id}",
                        "icon" to fileUri.toString()
                    )
                    db.collection("course")
                        .add(course)
                        .addOnSuccessListener {
                            Log.e(TAG, "${it.id}")
                            Toast.makeText(
                                this,
                                "The course has been added successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            var i = Intent(this, Lecturer_Dashboard::class.java)
                            startActivity(i)

                            //-----------------------------------------------------------------------------------------------------------------
                            //-----------------------------------------------------------------------------------------------------------------


//                                        val current = LocalDateTime.now()
//                                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
//                                        val formatted = current.format(formatter)
                            analytics.setUserProperty("User_type","Teacher")
                            analytics.logEvent("course_Adding") {
                                param("course_name", name_of_course)
                                param("course_id", id.toString() )
                                param("Date_Time",formatted)

                            }
                            //-----------------------------------------------------------------------------------------------------------------


                            //-----------------------------------------------------------------------------------------------------------------


                        }


                        .addOnFailureListener {
                            Log.e(TAG, "The add process is failed -> ${it}")
                            Toast.makeText(this, "Adding the course failed", Toast.LENGTH_LONG).show()
                        }
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK){
            IMAGE_URI = data!!.data
            icon_course.setImageURI(IMAGE_URI)
        }
    }

    //**********************************************

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu_lecturer,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var i = Intent()
        if (item.itemId == R.id.dashboard_lecturer){
            i = Intent(this,Lecturer_Dashboard::class.java)

        }else if (item.itemId == R.id.all_users_lec_menu_item){
            i = Intent(this,Users_Dashboard::class.java)

        }else if (item.itemId == R.id.Add_menu_item){
            i = Intent(this,Add_Course::class.java)

        }else if (item.itemId == R.id.all_courses_lec_menu_item){
            i = Intent(this,All_courses_for_lecturer::class.java)

        }else if(item.itemId == R.id.all_lec){
            i = Intent(this,All_teachers::class.java)

        }else if (item.itemId == R.id.all_std){
            i = Intent(this,All_student::class.java)

        }
        startActivity(i)
        return super.onOptionsItemSelected(item)

    }

    //**********************************************
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