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

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // Initialize Firebase Auth
        auth = Firebase.auth


        //First we bind the variables to the buttons ids
        //We use val instead of var because we want them to be "read-only"
        val btnSmoke : ImageButton = findViewById(R.id.smokingButton)
        val btnAlcohol : ImageButton = findViewById(R.id.alcoholButton)
        val btnDrugs : ImageButton = findViewById(R.id.drugButton)

        val db = Firebase.firestore
        val user = Firebase.auth.currentUser

        //It makes the buttons responsive on touch and open a new activity
        btnSmoke.setOnClickListener {
            if (user != null) {
                val ds = db.collection("users").document(user.uid)
                    .set(hashMapOf("vice" to "smoking"))
            }
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

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.i("useremail", currentUser.email)
        }
        Log.i("user", currentUser.toString())
    }

    override fun onBackPressed() { }
}
