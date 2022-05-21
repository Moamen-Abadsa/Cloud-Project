package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.study_room.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Lecturer
import kotlinx.android.synthetic.main.activity_lecturer_sign_in.*
import kotlinx.android.synthetic.main.activity_student_sign_in.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Lecturer_Sign_in : AppCompatActivity() {
    var bool = false

    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "lecturer"

    var Current_teacher_id = ""
    var data = ArrayList<Lecturer>()
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecturer_sign_in)
        db = Firebase.firestore
        // Obtain the FirebaseAnalytics instance.
        analytics = Firebase.analytics
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)
        btn_sign_in_lect.setOnClickListener {




            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("Terms of use")
            //set message for alert dialog
            builder.setMessage("Do you agree to the terms of use of the application")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->
                var mobileIn = lect_sign_in_mobile_no.text.toString()
                var passwordIn = lect_sign_in_password.text.toString()

                db.collection(myCollection)
                    .get()
                    .addOnSuccessListener {
                        for (document in it) {
                            val id = document.id
                            var mobile_no = document.get("mobile_no").toString()
                            var password  = document.get("password").toString()
                            var email     = document.get("email").toString()
                            if (mobileIn == mobile_no && passwordIn== password ) {
                                Current_teacher_id = mobile_no
                                db.collection(myCollection).document(id).update("active" , true)
                                    .addOnSuccessListener {
                                        Global.id = mobile_no
                                        var i = Intent(applicationContext, Lecturer_Dashboard::class.java)
                                        i.putExtra("mobile_number",mobile_no)
                                        startActivity(i)



                                        //-----------------------------------------------------------------------------------------------------------------

                                        analytics.setUserProperty("User_type","Teacher")
                                        analytics.logEvent("teacher_login") {
                                            param("email_name_teacher", email )
                                            param("mobile_number_teacher", mobile_no )
                                            param("Date_Time",formatted)

                                        }


                                        //-----------------------------------------------------------------------------------------------------------------


                                    }
                                    .addOnFailureListener {

                                    }
//                            var i = Intent(this,signin_or_signup::class.java)
//                            startActivity(i)
                                break
                            }
                        }
//                    if (bool == false){
//                        Toast.makeText(this,"البيانات التي أدخلتها غير صحيحة",Toast.LENGTH_LONG).show()
//
//                    }

                    }
                    .addOnFailureListener {
                        Toast.makeText(this,"هناك مشكلة في جلب البيانات من ال FireStore",Toast.LENGTH_LONG).show()
                    }

            }
            //performing cancel action
            builder.setNeutralButton("Cancel"){dialogInterface , which ->


            }
            //performing negative action
            builder.setNegativeButton("No"){dialogInterface, which ->


            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(true)

            alertDialog.show()








        }


    }
}


