package com.evdev.viceless.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.evdev.viceless.R
import com.evdev.viceless.utils.flags
import com.evdev.viceless.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var  mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        button_register.setOnClickListener {
            val email = edit_text_email.text.toString().trim()
            val password = edit_text_password.text.toString().trim()

            if(email.isEmpty()){
                edit_text_email.error = "Email required"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edit_text_email.error = "Valid Email required"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty() || password.length < 8){
                edit_text_password.error = "8 character password required"
                edit_text_password.requestFocus()
                return@setOnClickListener
            }

            registerUser(email, password)
        }

        text_view_login.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    private fun registerUser(email: String, password: String){
        progressbar.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                progressbar.visibility = View.GONE
                if(task.isSuccessful){
//                    saveUserToFirebaseDatabase(email)
                    flags()
                }else{
                    task.exception?.message?.let{
                        toast(it)
                    }
                }
            }

    }

//    private fun saveUserToFirebaseDatabase(email: String) {
//        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: "Null UID"
//        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
//        val user = User(uid, email, "-1", "-1", "-1")
//
//        ref.setValue(user)
//    }


    override fun onStart() {
        super.onStart()

        mAuth.currentUser?.let{
            flags()
        }
    }
}

//class User(val uid: String, val username: String, answer1: String, answer2: String, answer3: String)