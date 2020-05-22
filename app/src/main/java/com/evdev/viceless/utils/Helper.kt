package com.evdev.viceless.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.evdev.viceless.activities.HomePageActivity
import com.evdev.viceless.activities.LoginActivity

fun Context.toast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.flags(){
    val intent = Intent(this, HomePageActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}

fun Context.flagsLogOut(){
    val intent = Intent(this, LoginActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}