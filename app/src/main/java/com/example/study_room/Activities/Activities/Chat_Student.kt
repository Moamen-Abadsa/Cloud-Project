package com.example.study_room.Activities.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realTime_fcm.NotificationData
import com.example.realTime_fcm.PushNotification
import com.example.realTime_fcm.RetrofitInstance
import com.example.study_room.Activities.Activities.Adapters.Chat_Adapter_Student
import com.example.study_room.Activities.Activities.Adapters.Lectures_Adapter_lec
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import data.Chat_data
import kotlinx.android.synthetic.main.activity_add_course.*
import kotlinx.android.synthetic.main.activity_all_lectures_for_lec.*
import kotlinx.android.synthetic.main.activity_chat_student.*
import kotlinx.android.synthetic.main.activity_chat_teacher.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class Chat_Student : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "chat"
    var data = ArrayList<Chat_data>()
    val topic = "/topics/all"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_student)
        db = Firebase.firestore
        rv_chat_student.layoutManager = LinearLayoutManager(this)
        FirebaseMessaging.getInstance().subscribeToTopic(topic)

        Log.e(TAG,Global.id + "gggggggggggggggggggggg")

        var counter = -5

        var i = intent
        var receiver = i.getStringExtra("receiver").toString()


        db.collection("student").get().addOnFailureListener {}
            .addOnSuccessListener {
                for (doc in it) {
                    var std_id = doc.get("mobile_no").toString()
                    if (std_id == receiver) {
                        var first = doc.get("first").toString()
                        var middle = doc.get("middle").toString()
                        var last = doc.get("last").toString()
                        var name = "$first $middle $last"
                        name_chat.setText(name)

                    }

                }

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

                db.collection(myCollection).orderBy("time").get().addOnFailureListener {}
                    .addOnSuccessListener {
                        for (doc in it) {
                            var sender = doc.get("sender_id").toString()
                            var myReceiver = doc.get("receiver_id").toString()
                            if (Global.id == sender && receiver == myReceiver){
                                var message = doc.get("message").toString()
                                var chat_obj = Chat_data(sender, myReceiver, message,System.currentTimeMillis().toDouble())
                                data.add(chat_obj)
                            }else if (receiver == sender && myReceiver == Global.id){
                                var message = doc.get("message").toString()
                                var chat_obj = Chat_data(receiver, Global.id, message,System.currentTimeMillis().toDouble())
                                data.add(chat_obj)
                            }

                        }
                        var adapter = Chat_Adapter_Student(this, data)
                        rv_chat_student.adapter = adapter
                    }






                send_chat.setOnClickListener {
                    Log.e(TAG,Global.id + "gggggggggggggggggggggg")
                    var message = enter_text_chat.text.toString()

                    var chat = Chat_data(Global.id, receiver, message)
                    data.add(chat)
                    db.collection(myCollection).add(chat.ChatToMap()).addOnFailureListener {}
                        .addOnSuccessListener {
                            PushNotification(
                                NotificationData(
                                    "${Global.id}",
                                    "${message}"),
                                topic).also {
                                sendNotification(it)
                            }

                            sendNotification(PushNotification(
                                NotificationData(
                                "${Global.id}",
                                "${message}"),
                                topic).also { sendNotification(it) })
                            enter_text_chat.setText("")
                        }
//                     var adapter = Chat_Adapter_Student(this, data)
//                     rv_chat_student.adapter = adapter
                }


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






























/*




            send_chat.setOnClickListener {

                //Log.e(TAG,"You are pressed me")
                if (std_id == null) {
                    //  Log.e(TAG,"You are lecture")
                    user_image.setImageResource(R.drawable.teacher)
                    var second_id = lec_id
                    db.collection(myCollection).get().addOnFailureListener {}.addOnSuccessListener {
                        for (doc in it) {
                            var messages = doc.get("messageList") as ArrayList<String>
                            for (m in messages) {
                                var last_message = m
                                var id_chat = "$first_id $second_id"
                                data.add(Chat_data(first_id, second_id!!, messages, id_chat, last_message))

                                if (id_chat == "$first_id $second_id") {
                                    var adapter = Chat_Adapter(this, data, true)
                                    rv_chat.adapter = adapter
                                } else {
                                    var adapter = Chat_Adapter(this, data, false)
                                    rv_chat.adapter = adapter
                                }

                            }

                        }
                    }



                    db.collection("lecturer").get().addOnFailureListener { }.addOnSuccessListener {
                        for (doc in it) {
                            var mob = doc.get("mobile_no").toString()
                            if (mob == second_id) {
                                var f = doc.get("first").toString()
                                var m = doc.get("middle").toString()
                                var l = doc.get("last").toString()

                                name_chat.setText("$f $m $l").toString()

                            }
                        }
                    }

                db.collection("chat").get().addOnFailureListener {}.addOnSuccessListener {
                    Log.e(TAG, "You are pressed me 1 ")

                    for (doc in it) {
                        Log.e(TAG, "You are pressed me 2 ")
                        var id_chat = doc.get("id_chat").toString()
                        if (id_chat == "$first_id $second_id" || id_chat == "$second_id $first_id") {
                            Log.e(TAG, "You are pressed me 3 ")

                            var id = doc.id
                            var message_List = doc.get("messageList") as ArrayList<String>
                            var message_text = enter_text_chat.text.toString()
                            message_List.add(message_text)
                            db.collection(myCollection).document(id).update(
                                "messageList", message_List,
                                "last_Message", message_text
                            ).addOnFailureListener {}
                                .addOnSuccessListener {
                                    enter_text_chat.setText("")

                                    db.collection(myCollection).get().addOnFailureListener {}
                                        .addOnSuccessListener {
                                            for (doc in it) {
                                                if (id_chat == "$first_id $second_id") {
                                                    var message_List2 =
                                                        doc.get("messageList") as ArrayList<String>

                                                    data.add(
                                                        Chat_data(
                                                            first_id,
                                                            second_id!!,
                                                            message_List2,
                                                            id_chat,
                                                            message_text
                                                        )
                                                    )
                                                    var adapter = Chat_Adapter(this, data, true)
                                                    rv_chat.adapter = adapter
                                                } else if (id_chat == "$second_id $first_id") {
                                                    var message_List2 =
                                                        doc.get("messageList") as ArrayList<String>

                                                    data.add(
                                                        Chat_data(
                                                            first_id,
                                                            second_id!!,
                                                            message_List2,
                                                            id_chat,
                                                            message_text
                                                        )
                                                    )
                                                    var adapter = Chat_Adapter(this, data, false)
                                                    rv_chat.adapter = adapter
                                                }

                                            }
                                        }
                                }
                        }
                    }
                }
            }else if (lec_id == null){
                    Log.e(TAG,"You are student")

            user_image.setImageResource(R.drawable.student)
            var second_id = std_id

            db.collection(myCollection).get().addOnFailureListener {}.addOnSuccessListener {
                for (doc in it){
                    var messages = doc.get("messageList") as ArrayList<String>
                    for (m in messages){
                        var last_message = m
                        var id_chat = "$first_id $second_id"
                        data.add(Chat_data(first_id, second_id!!, messages,id_chat,last_message))


                    }
                    var adapter = Chat_Adapter(this, data,false)
                    rv_chat.adapter = adapter
                }
            }

            db.collection("student").get().addOnFailureListener {}.addOnSuccessListener {
                for (doc in it){
                    var mob = doc.get("mobile_no").toString()
                    if (mob == second_id){
                        Log.e(TAG,mob + " mobbbbbb")

                        var f = doc.get("first").toString()
                        var m = doc.get("middle").toString()
                        var l = doc.get("last").toString()

                        name_chat.setText("$f $m $l").toString()

                    }
                }
            }
                    db.collection("chat").get().addOnFailureListener {}.addOnSuccessListener {
                        Log.e(TAG,"You are pressed me 11 ")

                        for (doc in it){
                            Log.e(TAG,"You are pressed me 22 ")
                            var id_chat = doc.get("id_chat").toString()
                            if (id_chat == "$second_id $first_id" || id_chat == "$first_id $second_id"){
                                Log.e(TAG,"You are pressed me 33 ")

                                var id = doc.id
                                var message_List = doc.get("messageList") as ArrayList<String>
                                var message_text = enter_text_chat.text.toString()
                                message_List.add(message_text)
                                db.collection(myCollection).document(id).update("messageList",message_List,
                                    "last_Message",message_text).addOnFailureListener {}
                                    .addOnSuccessListener {
                                        enter_text_chat.setText("")

                                        db.collection(myCollection).get().addOnFailureListener {}
                                            .addOnSuccessListener {
                                                for (doc in it){
                                                    if (id_chat == "$first_id $second_id"){
                                                        var message_List2 = doc.get("messageList") as ArrayList<String>

                                                        data.add(Chat_data(first_id,second_id!!,message_List2,id_chat,message_text))
                                                        var adapter = Chat_Adapter(this, data,true)
                                                        rv_chat.adapter = adapter
                                                    }else if(id_chat == "$second_id $first_id"){
                                                        var message_List2 = doc.get("messageList") as ArrayList<String>

                                                        data.add(Chat_data(first_id,second_id!!,message_List2,id_chat,message_text))
                                                        var adapter = Chat_Adapter(this, data,false)
                                                        rv_chat.adapter = adapter
                                                    }

                                                }
                                            }
                                    }
                            }
                        }
                    }

                }






}

 */

//
