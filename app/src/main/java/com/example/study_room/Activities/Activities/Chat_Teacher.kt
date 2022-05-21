package com.example.study_room.Activities.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realTime_fcm.NotificationData
import com.example.realTime_fcm.PushNotification
import com.example.realTime_fcm.RetrofitInstance
import com.example.study_room.Activities.Activities.Adapters.Chat_Adapter_Student
import com.example.study_room.Activities.Activities.Adapters.Chat_Adapter_Teacher
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import data.Chat_data
import kotlinx.android.synthetic.main.activity_chat_student.*
import kotlinx.android.synthetic.main.activity_chat_teacher.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class Chat_Teacher : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "chat"
    var data = ArrayList<Chat_data>()
    val topic = "/topics/all"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_teacher)
        db = Firebase.firestore
        rv_chat_student_teacher.layoutManager = LinearLayoutManager(this)
        FirebaseMessaging.getInstance().subscribeToTopic(topic)

        Log.e(TAG,"${Global.id} Gloooooobaaale")
        var counter = -5

        var i = intent
        var receiver = i.getStringExtra("receiver").toString()


        db.collection("lecturer").get().addOnFailureListener {}
            .addOnSuccessListener {
                for (doc in it) {
                    var std_id = doc.get("mobile_no").toString()
                    if (std_id == receiver) {
                        var first = doc.get("first").toString()
                        var middle = doc.get("middle").toString()
                        var last = doc.get("last").toString()
                        var name = "$first $middle $last"
                        name_chat_teacher.setText(name)

                    }

                }
            }
                db.collection("student").get().addOnFailureListener {}
                    .addOnSuccessListener {
                        for (doc in it) {
                            var std_id = doc.get("mobile_no").toString()
                            if (std_id == receiver) {
                                var first = doc.get("first").toString()
                                var middle = doc.get("middle").toString()
                                var last = doc.get("last").toString()
                                var name = "$first $middle $last"
                                name_chat_teacher.setText(name)

                            }

                        }
                    }

                        db.collection(myCollection).orderBy("time").get().addOnFailureListener {}
                            .addOnSuccessListener {
                                for (doc in it) {
                                    var sender = doc.get("sender_id").toString()
                                    var myReceiver = doc.get("receiver_id").toString()
                                   if (Global.id == sender && receiver == myReceiver ) {
                                        var message = doc.get("message").toString()
                                        var chat_obj = Chat_data(Global.id, myReceiver, message,System.currentTimeMillis().toDouble())
                                        data.add(chat_obj)

                                    }else if (receiver == sender && myReceiver == Global.id){
                                       var message = doc.get("message").toString()
                                       var chat_obj = Chat_data(receiver, Global.id, message,System.currentTimeMillis().toDouble())
                                       data.add(chat_obj)
                                   }


                                }
                                var adapter = Chat_Adapter_Teacher(this, data)
                                rv_chat_student_teacher.adapter = adapter
                            }






                        send_chat_teacher.setOnClickListener {
                            var message = enter_text_chat_teacher.text.toString()

                            var chat = Chat_data(Global.id, receiver, message)
                            data.add(chat)
                            db.collection(myCollection).add(chat.ChatToMap())
                                .addOnFailureListener {}
                                .addOnSuccessListener {
                                    enter_text_chat_teacher.setText("")
                                    PushNotification(
                                        NotificationData(
                                            "${Global.id}",
                                            "${message}"),
                                        topic).also {
                                        sendNotification(it)
                                    }

                                    sendNotification(
                                        PushNotification(
                                        NotificationData(
                                            "${Global.id}",
                                            "${message}"),
                                        topic).also { sendNotification(it) })
                                    enter_text_chat_teacher.setText("")
                                }
                            var adapter = Chat_Adapter_Teacher(this, data)
                            rv_chat_student_teacher.adapter = adapter
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

