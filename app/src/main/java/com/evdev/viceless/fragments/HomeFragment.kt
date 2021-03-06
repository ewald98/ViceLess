package com.evdev.viceless.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.evdev.viceless.R
import com.evdev.viceless.alcohol.AlcoholHomeActivity
import com.evdev.viceless.drugs.DrugsHomeActivity
import com.evdev.viceless.smoking.SmokingHomeActivity
import com.evdev.viceless.smoking.SmokingIntroActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        smokingButton.setOnClickListener{
            activity?.let {
                checkForAnswers(it)
            }
        }
        alcoholButton.setOnClickListener{
            activity?.let {
                val intent = Intent (it, AlcoholHomeActivity::class.java)
                it.startActivity(intent)
            }
        }
        drugButton.setOnClickListener{
            activity?.let {
                val intent = Intent (it, DrugsHomeActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

    private fun checkForAnswers(it: FragmentActivity){
        val db = FirebaseFirestore.getInstance()
        val uID = FirebaseAuth.getInstance().currentUser?.uid
        db.collection("users").document(uID!!).get()
            .addOnSuccessListener { result ->
                    if (result["Cigs_Cost"] != "" && result["Cigs_Smoked"] != ""
                        && result["Smoke_Time"] != "" && result["ID"] != null) {
                        startActivity(Intent(it, SmokingHomeActivity::class.java))
                    } else {
                        startActivity(Intent(it, SmokingIntroActivity::class.java))
                    }

            }
    }

}
