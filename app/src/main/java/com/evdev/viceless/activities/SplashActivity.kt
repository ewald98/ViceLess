package com.evdev.viceless.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.evdev.viceless.R
import com.evdev.viceless.smoking.SmokingHomeActivity
import com.evdev.viceless.smoking.SmokingIntroActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIMER:Long = 2000 //setting de delay variable for 1 seconds
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val db = FirebaseFirestore.getInstance()
            val uID = FirebaseAuth.getInstance().currentUser?.uid
            if(uID != null){
                db.collection("users").document(uID).get()
                    .addOnSuccessListener { result ->
                        if(result["ChosenVice"] == "Smoke" && result["Cigs_Cost"] != ""
                            && result["Cigs_Smoked"] != "" && result["Smoke_Time"] != "") {
                            startActivity(Intent(this, SmokingHomeActivity::class.java))
                        } else if(result["ChosenVice"] == "Smoke"){
                            startActivity(Intent(this, SmokingIntroActivity::class.java))
                        }else {
                            startActivity(Intent(this, HomePageActivity::class.java))
                        }
                    }
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
                }
            finish()
        }, SPLASH_TIMER)
    }
}
