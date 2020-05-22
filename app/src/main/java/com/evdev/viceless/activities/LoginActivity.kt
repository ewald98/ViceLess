package com.evdev.viceless.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.evdev.viceless.*
import com.evdev.viceless.utils.flags
import com.evdev.viceless.utils.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        button_sign_in.setOnClickListener {
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

            loginUser(email, password)
        }

        text_view_register.setOnClickListener{
            startActivity(Intent(this@LoginActivity,
                RegisterActivity::class.java))
        }
        text_view_forget_password.setOnClickListener {
            startActivity(Intent(this@LoginActivity,
                ResetPasswordActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String){
        progressbar.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                progressbar.visibility = View.GONE
                if(task.isSuccessful){
                    flags()
                }else{
                    task.exception?.message?.let{
                        toast(it)
                    }
                }
            }
    }
    override fun onStart() {
        super.onStart()

        mAuth.currentUser?.let{
            flags()
        }
    }
}
