package com.evdev.viceless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_home_page.*

class HomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        //First we bind the variables to the buttons ids
        //We use val instead of var because we want them to be "read-only"
        val btnSmoke : ImageButton = findViewById(R.id.smokingButton)
        val btnAlcohol : ImageButton = findViewById(R.id.alcoholButton)
        val btnDrugs : ImageButton = findViewById(R.id.drugButton)

        //It makes the buttons responsive on touch and open a new activity
        btnSmoke.setOnClickListener {
            val smoke = Intent(this, SmokingIntroActivity :: class.java)
            startActivity(smoke)
        }
        btnAlcohol.setOnClickListener {
            val alcohol = Intent(this, MainActivity :: class.java)
            startActivity(alcohol)
        }
        btnDrugs.setOnClickListener {
            val drugs = Intent(this, MainActivity :: class.java)
            startActivity(drugs)
        }

    }
}
