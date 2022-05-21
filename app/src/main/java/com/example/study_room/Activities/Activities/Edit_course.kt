package com.example.study_room.Activities.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import data.Course
import kotlinx.android.synthetic.main.activity_add_lecture.*
import kotlinx.android.synthetic.main.activity_edit_course.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.log

class Edit_course : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "course"
    var data = ArrayList<Course>()

    var fileUri: Uri? = null
    var IMAGE_URI: Uri? = null

    private val PICK_IMAGE_REQUEST = 111
    val topic = "/topics/all"
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_course)
        db = Firebase.firestore
        FirebaseMessaging.getInstance().subscribeToTopic(topic)

        analytics = Firebase.analytics
        val storage = Firebase.storage
        val StorageRef = storage.reference
        val imageRef = StorageRef.child("icons")

        var i = intent
        var id =   i.getStringExtra("id").toString()
        var name = i.getStringExtra("name").toString()
        var desc = i.getStringExtra("desc").toString()
        var icon = i.getStringExtra("icon").toString()
        var isHide = i.getStringExtra("isHide").toBoolean()

        Log.e(TAG, "${name} 5555 ")
        edit_name_course.setText(name).toString()
        edit_desc_course.setText(desc).toString()
        Picasso.get().load(icon).into(edit_icon_course)




        edit_icon_course.setOnClickListener {
            var i = Intent()
            i.action = Intent.ACTION_PICK
            i.type = "image/*"
            startActivityForResult(i, PICK_IMAGE_REQUEST)

        }



        btn_edit_course.setOnClickListener {

            // Get the data from an ImageView as bytes )
            val bitmap = (edit_icon_course.drawable as BitmapDrawable).bitmap
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
                    Log.e("MSA", fileUri.toString() + " . 222")

                    var name_edit = edit_name_course.text.toString()
                    Log.e("MSA", id + " . 222")

                    var desc_edit = edit_desc_course.text.toString()

                    db.collection(myCollection).get().addOnFailureListener {  }.addOnSuccessListener {
                        for (doc in it){
                            if (id == doc.get("id").toString()){
                                var my_id = doc.id
                                db.collection("course").document(my_id).update(
                                    "desc_of_course", desc_edit,
                                    "name_of_course", name_edit, "icon", fileUri.toString())
                                    .addOnSuccessListener {
                                        var i = Intent(this, All_courses_for_lecturer::class.java)
                                        startActivity(i)
                                        Toast.makeText(
                                            this,
                                            "The course has been editted successfully",
                                            Toast.LENGTH_LONG
                                        ).show()



                                        val current = LocalDateTime.now()
                                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                                        val formatted = current.format(formatter)


                                        PushNotification(
                                            NotificationData(
                                                "Editting Course",
                                                "The ${name} course has been editted at ${formatted}"),
                                            topic).also {
                                            sendNotification(it)
                                        }

                                        sendNotification(PushNotification(
                                            NotificationData(
                                                "Editting Course",
                                                "The ${name} course has been editted at ${formatted}"),
                                            topic).also { sendNotification(it) })

                                        //-----------------------------------------------------------------------------------------------------------------


//                                        val current = LocalDateTime.now()
//                                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
//                                        val formatted = current.format(formatter)
                                        analytics.setUserProperty("User_type","Teacher")
                                        analytics.logEvent("course_editting") {
                                            param("course_name", name_edit)
                                            param("course_id", desc_edit)
                                            param("Date_Time",formatted)

                                        }
                                        //-----------------------------------------------------------------------------------------------------------------

                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            this,
                                            "The course has not been editted successfully",
                                            Toast.LENGTH_LONG
                                        ).show()

                                    }
                            }
                        }
                    }



                }


//            db.collection(myCollection).document(id).update("name_of_course" , name , "desc_of_course" ,
//                desc,"icon" , icon).addOnFailureListener {  }.addOnSuccessListener {
//
//            }
            }


        }
    }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
                IMAGE_URI = data!!.data
                edit_icon_course.setImageURI(IMAGE_URI)
            }
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
