package com.example.study_room.Activities.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.Toast
import android.widget.VideoView
import com.example.study_room.R
import com.google.android.gms.common.internal.Constants
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import data.Lecture
import data.Lecturer
import kotlinx.android.synthetic.main.activity_add_lecture.*
import java.util.HashMap

class Add_Lecture : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "lecture"
    var progressDialog: ProgressDialog? = null

    private lateinit var analytics: FirebaseAnalytics


    var fileUri : Uri? = null
    var videoUrl : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_lecture)
        db = Firebase.firestore

        val storage = Firebase.storage
        val storageRef = storage.reference
        analytics = Firebase.analytics

        var i = intent
        var course_id = i.getStringExtra("id_course").toString()


        add_video.setOnClickListener {
            chooseVideo()
        }
        edit_assignment_lecture.setOnClickListener {
            chooseFile()
        }

        submitt_add_lec.setOnClickListener {
            var name = edit_name_lecture.text.toString()
            var desc = edit_desc_lecture.text.toString()

            var video = add_video.text.toString()
            var new_video_uri = Uri.parse(video)

            var file = edit_assignment_lecture.text.toString()
            var new_file_uri = Uri.parse(file)
            var id = (0..9999999).random()
            var student_view = ArrayList<String>()

            storageRef!!.child("video/$id").putFile(new_video_uri!!)
                .addOnSuccessListener { taskSnapshot ->
                    //progressDialog.dismiss()
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        var uri_video = uri.toString()
                        var lecture = Lecture(name,desc,file.toString(),uri_video,course_id,"${(0..9999999).random()}",student_view,false)

                        db.collection(myCollection).add(lecture.LecturetoMap()).addOnFailureListener {
                            Toast.makeText(this,"عملية إضافة المحاضرة لم تنجح أخي الكريم",Toast.LENGTH_LONG).show()
                        }.addOnSuccessListener {

                            var my_id = it.id

                            storageRef!!.child("files/$id").putFile(new_file_uri!!)
                                .addOnSuccessListener { taskSnapshot ->
                                    //progressDialog.dismiss()
                                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                                        var uri_file = uri.toString()
                                        db.collection(myCollection).document(my_id).update("assignment",uri_file)

                                    }
                                    analytics.setUserProperty("User_type","Teacher")
                                    analytics.logEvent("adding_lecture") {
                                        param("lecture_name", name)
                                        param("lecture_id", desc)

                                    }
                                }

                            var i = Intent(this,All_lectures_for_lec::class.java)
                            i.putExtra("course_id",course_id)
                            startActivity(i)

                            //-----------------------------------------------------------------------------------------------------------------




                            //-----------------------------------------------------------------------------------------------------------------


                        }
                    }
                }




        }


    }

    private fun chooseVideo() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 5000)
    }

    private fun chooseFile() {
        val intent = Intent()
        intent.type = "application/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 4000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 5000 && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            videoUrl = data.data
            add_video.append(videoUrl.toString())


        } else if (requestCode == 4000 && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            fileUri = data.data
            edit_assignment_lecture.append(fileUri.toString())


        }
    }

}





