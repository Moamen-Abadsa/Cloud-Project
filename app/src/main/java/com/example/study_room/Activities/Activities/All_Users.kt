package com.example.study_room.Activities.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.study_room.R

class All_Users : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_users)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var i = Intent()
        if (item.itemId == R.id.home){
            i = Intent(this,Student_Dashboard::class.java)
        }else if (item.itemId == R.id.all_courses_student){
            i = Intent(this,Users_Dashboard::class.java)

        }else if (item.itemId == R.id.selected_courses){
            i = Intent(this,Selected_courses::class.java)

        }else if (item.itemId == R.id.all_users_student){
            i = Intent(this,All_Users::class.java)

        }
        startActivity(i)
        return super.onOptionsItemSelected(item)

    }
}