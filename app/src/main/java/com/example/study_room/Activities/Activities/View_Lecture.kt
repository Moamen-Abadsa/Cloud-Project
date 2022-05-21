package com.example.study_room.Activities.Activities

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.study_room.Activities.Activities.Adapters.upload_file
import com.example.study_room.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import data.Lecture
import kotlinx.android.synthetic.main.activity_view_lecture.*

class View_Lecture : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "lecture"
    var data = ArrayList<Lecture>()
    var fileUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_lecture)
        db = Firebase.firestore

        val storage = Firebase.storage
        val storageRef = storage.reference


        var i = intent
        var id_lec = i.getStringExtra("id").toString()

        db.collection(myCollection).get().addOnFailureListener { }.addOnSuccessListener {
            for (doc in it) {
                var lecture_id = doc.get("lecture_id").toString()
                if (id_lec == lecture_id) {
                    var name = doc.get("name").toString()
                    var desc = doc.get("desc").toString()
                    var isHide = doc.get("isHide").toString()
                    var file = doc.get("assignment").toString()
                    var video = doc.get("video").toString()
                    var student_view = doc.get("student_view") as ArrayList<String>

                    view_name_lec.setText(name).toString()
                    view_desc_lec.setText(desc).toString()

                    val player = ExoPlayer.Builder(this).build()
                    video_view_lec.player = player
                    val mediaItem: MediaItem =
                        MediaItem.fromUri(Uri.parse("${video}"))
                    player.setMediaItem(mediaItem)
                    player.prepare()


                    upload_image.setOnClickListener {
                        chooseFile()
                        //-----------------------------------------------------------------------------------------------------------------


                        //-----------------------------------------------------------------------------------------------------------------

                    }
                }

                upload_file.setOnClickListener {
                    var my_file = file_uri_text.text.toString()
                    var my_new_file = Uri.parse(my_file)
                    Log.e(TAG,"$my_file 999999999")
                    storageRef.child("ulpoad/").putFile(my_new_file)
                        .addOnSuccessListener { taskSnapshot ->
                            //progressDialog.dismiss()
                            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                                var uri_file = uri.toString()
                                db.collection("student").get().addOnSuccessListener {
                                    for (doc in it){
                                        var id = doc.get("id").toString()
                                        if (id == Global.id){
                                            var std_id = doc.get("mobile_no").toString()
                                            var file = upload_file(uri_file,std_id,lecture_id,)

                                            db.collection("upload_file").add(file.FiletoMap())
                                                .addOnFailureListener {  }
                                                .addOnSuccessListener {  }

                                        }
                                    }
                                }

                            }
                        }
                }

                download.setOnClickListener {

                    db.collection(myCollection).get().addOnFailureListener { }
                        .addOnSuccessListener {
                            for (doc in it) {
                                var lecture_id = doc.get("lecture_id").toString()
                                if (id_lec == lecture_id) {
                                    var update_id = doc.id
                                    var name = doc.get("name").toString()
                                    var desc = doc.get("desc").toString()
                                    var isHide = doc.get("isHide").toString()
                                    var file = doc.get("assignment").toString()
                                    var video = doc.get("video").toString()
                                    var student_view = doc.get("student_view") as ArrayList<String>
                                    view_name_lec.setText(name).toString()
                                    view_desc_lec.setText(desc).toString()


                                    var request = DownloadManager.Request(Uri.parse(file))
                                        .setTitle("Download")
                                        .setDescription("Download a ${name}")
                                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                        .setAllowedOverMetered(true)
                                    val imageurl: String = file.toString()

                                    val dm =
                                        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                                    dm!!.enqueue(request)

                                    db.collection("student").get().addOnFailureListener {  }.addOnSuccessListener {
                                        for (doc in it){
                                           var id =  doc.get("mobile_no").toString()
                                            if (id == Global.id){
                                                var std_id = doc.get("mobile_no").toString()
                                                student_view.add(std_id)
                                                db.collection(myCollection)
                                                    .document(update_id).update("student_view",student_view)

                                            }
                                        }
                                    }
                                }


                            }





                        }
                }


            }
        }
    }

    private fun chooseFile() {
        val intent = Intent()
        intent.type = "application/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 4000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
         if (requestCode == 4000 && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            fileUri = data.data
              file_uri_text.append(fileUri.toString())


        }
    }
}