package com.evdev.viceless

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.evdev.viceless.smoking.SmokingHomeActivity
import com.evdev.viceless.smoking.SmokingIntroActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.zzd.getToken
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    private val SPLASH_TIMER:Long = 1000 //setting de delay variable for 2 seconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = auth.currentUser
        if (currentUser != null) {
            // user already logged in, can redirect further
            Log.i("before_login_useremail", currentUser!!.email)
            Handler().postDelayed({
                next_activity()
            }, SPLASH_TIMER)
        } else {
            // login
            Log.i("before_login_useremail", "nothing")
            Handler().postDelayed({
                val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

                // Create and launch sign-in intent
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                    RC_SIGN_IN
                )
            }, SPLASH_TIMER)
        }
    }

    fun next_activity() {
         /*

         this fun verifies if vice is selected and if introSlide values are initiated
         and redirects to the correct activity:

         * HomePageActivity if no vice is selected
         * SmokingIntroActivity if vice is selected, but slide values are not set
         * SmokingHomeActivity else (if vice is selected and slide values are set)

         */


        // fetch data from user:
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser

        val ds = db.collection("users").document(user!!.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("getsnap", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("getsnap", "No such document")
                }

                if (!document.contains("vice")!!) {
                    Intent(applicationContext, HomePageActivity::class.java).also {
                        startActivity(it)
                    }
                } else {
                    if (document.contains("avgCigsPerDay")!! and
                        document.contains("avgPackCost")!! and
                        document.contains("yearsBeingSmoker")!!)
                        Intent(applicationContext, SmokingHomeActivity::class.java).also {
                            startActivity(it)
                        }
                    else
                        Intent(applicationContext, SmokingIntroActivity::class.java).also {
                            startActivity(it)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("getsnap", "get failed with ", exception)
            }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                next_activity()

//                 val user = FirebaseAuth.getInstance().currentUser
//                Intent(applicationContext, HomePageActivity::class.java).also {
//                    startActivity(it)
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Log.i("login failed", "well f***")
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }

}
