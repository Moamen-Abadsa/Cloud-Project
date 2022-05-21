package com.example.study_room.Activities.Activities

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_lecturer_sign_up.*
import kotlinx.android.synthetic.main.activity_student_sign_up.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayDeque

class Student_Sign_up : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "student"
    var data = ArrayList<Lecturer>()
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_sign_up)
        db = Firebase.firestore

        // Obtain the FirebaseAnalytics instance.
        analytics = Firebase.analytics

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)

        student_sign_up_birth.setOnClickListener {
            val currentDate = Calendar.getInstance()
            val day = currentDate.get(Calendar.DAY_OF_MONTH)
            val month = currentDate.get(Calendar.MONTH)
            val year = currentDate.get(Calendar.YEAR)


            val picker = DatePickerDialog(
                this,
                { datePicker, i, i2, i3 ->
                    val str_date = "$i/${1 + i2}/$i3"
                    student_sign_up_birth.setText(str_date) },
                year,
                month,
                day
            )
            picker.show()
        }
        btn_sign_up_student.setOnClickListener {
            var bool = true
            var first = student_sign_up_first.text.toString()
            var middle = student_sign_up_middle.text.toString()
            var last = student_sign_up_last.text.toString()
            var address = student_sign_up_address.text.toString()
            var mobile_no = student_sign_up_mobile_no.text.toString()
            var email = student_sign_up_email.text.toString()
            var password = student_sign_up_password.text.toString()
            var birth = student_sign_up_birth.text.toString()
            db.collection("student")
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        var old_mobile = document.get("mobile_no").toString()
                        if (old_mobile == mobile_no ){
                            bool = false
                            Toast.makeText(this,"من فضلك قم بتغيير رقم الجوال لأنه مكرر", Toast.LENGTH_LONG).show()
//                            var i = Intent(this,Student_Sign_up::class.java)
//                            startActivity(i)
                        }


                    }
                    var x = ArrayList<String>()
                    var student = Student(first,middle,last,birth,address,email,mobile_no,password,false,x)

                    var studentMap = student.StudenttoMap()
                    Log.e(TAG,bool.toString())
                    if (bool){
                        db.collection("student").add(studentMap).addOnSuccessListener {
                            var i = Intent(this,Student_Sign_in::class.java)
                            startActivity(i)
                        }.addOnFailureListener {

                        }
                    }

                    //-----------------------------------------------------------------------------------------------------------------

                    analytics.setUserProperty("User_type","Student")
                    analytics.logEvent("studnet_signup") {
                        param("email_name_student", email )
                        param("mobile_number_student", mobile_no )
                        param("Date_Time",formatted)

                    }


                    //-----------------------------------------------------------------------------------------------------------------


                }
                .addOnFailureListener {
                    Toast.makeText(this,"هناك مشكلة في جلب البيانات من ال FireStore", Toast.LENGTH_LONG).show()
                }




        }

    }
}