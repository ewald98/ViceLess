package com.evdev.viceless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import com.evdev.viceless.alcohol.AlcoholHomeActivity
import com.evdev.viceless.drugs.DrugsHomeActivity
import com.evdev.viceless.smoking.SmokingIntroActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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
//            if (user != null) {
//                val ds = db.collection("users").document(user.uid)
//                    .set(hashMapOf("vice" to "smoking"))
//            }
            val smoke = Intent(this, SmokingIntroActivity:: class.java)
            startActivity(smoke)
        }
        btnAlcohol.setOnClickListener {
            val alcohol = Intent(this, AlcoholHomeActivity:: class.java)
            startActivity(alcohol)
        }
        btnDrugs.setOnClickListener {
            val drugs = Intent(this, DrugsHomeActivity :: class.java)
            startActivity(drugs)
        }

    }

    override fun onBackPressed() { }
}