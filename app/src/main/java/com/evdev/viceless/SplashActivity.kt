package com.evdev.viceless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIMER:Long = 4000 //setting de delay variable for 2 seconds
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        /*We use the HANDLER function in order to start the main or next activity
        while the splash screen is loading*/

        Handler().postDelayed({
            startActivity(Intent(this, HomePageActivity::class.java))
            finish()
        }, SPLASH_TIMER)
    }
}
