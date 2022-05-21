package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.study_room.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.Lecturer
import kotlinx.android.synthetic.main.activity_signin_or_signup.*

class signin_or_signup : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    var TAG = "MSA"
    var myCollection = "student"
    var data = ArrayList<Lecturer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_or_signup)

        db = Firebase.firestore

        var i : Intent
        sign_in_img.setOnClickListener {
            i = Intent(this,Sign_in ::class.java)
            startActivity(i)
        }
        sign_up_img.setOnClickListener {
            i = Intent(this,Sign_up ::class.java)
            startActivity(i)
        }

        var id =""
        db.collection(myCollection)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                         id = document.id
                        db.collection(myCollection).document(id).update("active" , false)
                            .addOnSuccessListener {

                            }
                            .addOnFailureListener {

                            }

                    }
                }
            .addOnFailureListener {
                Toast.makeText(this,"هناك مشكلة في جلب البيانات من ال FireStore", Toast.LENGTH_LONG).show()
            }

        db.collection("lecturer")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                   var id_lec = document.id
                    db.collection("lecturer").document(id_lec).update("active" , false)
                        .addOnSuccessListener {

                        }
                        .addOnFailureListener {

                        }

                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"هناك مشكلة في جلب البيانات من ال FireStore", Toast.LENGTH_LONG).show()
            }


    }
}
