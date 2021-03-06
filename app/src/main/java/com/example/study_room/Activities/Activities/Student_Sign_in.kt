package com.example.study_room.Activities.Activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.study_room.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Lecturer
import data.Student
import kotlinx.android.synthetic.main.activity_lecturer_sign_in.*
import kotlinx.android.synthetic.main.activity_student_sign_in.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Student_Sign_in : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "student"

    var Current_Student_id = ""
    var data = ArrayList<Student>()
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_sign_in)
    // Obtain the FirebaseAnalytics instance.
        analytics = Firebase.analytics
        db = Firebase.firestore
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)
        var bool = false
        btn_sign_in_student.setOnClickListener {





            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("Terms of use")
            //set message for alert dialog
            builder.setMessage("Do you agree to the terms of use of the application")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->
                var mobileIn = std_sign_in_mobile_no.text.toString()
                var passwordIn = std_sign_in_password.text.toString()

                db.collection(myCollection)
                    .get()
                    .addOnSuccessListener {
                        for (document in it) {
                            var mobile_no = document.get("mobile_no").toString()
                            var password = document.get("password").toString()


                            if (mobileIn == mobile_no && passwordIn== password ){
                                Global.id = mobile_no
                                var i = Intent(this,Users_Dashboard::class.java)
                                val id = document.id

                              i.putExtra("id",id).toString()
                                    var first = document.get("first").toString()
                                    var middle = document.get("middle").toString()
                                    var last = document.get("last").toString()
                                    var email = document.get("email").toString()
                                    var address = document.get("address").toString()
                                    var birth = document.get("birth_date").toString()
                                    var name = "$first $middle $last"

                                db.collection(myCollection).document(id).update("active" , true)
                                    .addOnSuccessListener {
                                        //-----------------------------------------------------------------------------------------------------------------
                                        analytics.setUserProperty("User_type","Student")
                                        analytics.logEvent("studnet_login") {
                                            param("email_name_student", email )
                                            param("mobile_number_student", mobileIn )
                                            param("Date_Time",formatted)

                                        }

                                        //-----------------------------------------------------------------------------------------------------------------


                                        var i = Intent(this , Users_Dashboard::class.java)
                                        i.putExtra("mobile_number",mobile_no)
                                        startActivity(i)
                                        //Toast.makeText(this,"The book has been editted successfully",Toast.LENGTH_LONG).show()

                                    }
                                    .addOnFailureListener {
                                        // Toast.makeText(this,"The book has not been editted successfully",Toast.LENGTH_LONG).show()

                                    }
                                //startActivity(i)
                                Toast.makeText(this,"???????????????? ???????? ?????????????? ?????????????? ?? ??????????", Toast.LENGTH_LONG).show()
                                break
                            }else{
                                // Toast.makeText(this,"???????????????? ???????? ?????????????? ?????? ??????????", Toast.LENGTH_LONG).show()

                            }
                        }

                    }
                    .addOnFailureListener {
                        Toast.makeText(this,"???????? ?????????? ???? ?????? ???????????????? ???? ???? FireStore", Toast.LENGTH_LONG).show()
                    }
            }
            //performing cancel action
            builder.setNeutralButton("Cancel"){dialogInterface , which ->
                Toast.makeText(applicationContext,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
            }
            //performing negative action
            builder.setNegativeButton("No"){dialogInterface, which ->
                Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }










        }









