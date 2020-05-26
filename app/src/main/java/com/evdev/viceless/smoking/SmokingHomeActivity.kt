package com.evdev.viceless.smoking

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.evdev.viceless.R
import com.evdev.viceless.activities.HomePageActivity
import com.evdev.viceless.utils.SmokingDanger
import com.evdev.viceless.utils.Supplier.smokingDangers
import com.evdev.viceless.utils.flagsLogOut
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.activity_smoking_home.*
import java.util.*
import kotlin.math.roundToInt


class SmokingHomeActivity : AppCompatActivity() {

    var cigs_smoked_today = 0.0f
    private lateinit var smoked_today_increment_button: RelativeLayout
    private lateinit var smoking_stats_button: LinearLayout
    private lateinit var craving_button: LinearLayout
    private lateinit var smoked_today_textview: TextView

    private lateinit var progress_bar_today: CircularProgressBar
    private lateinit var progress_bar_yesterday: CircularProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedMoney(cigs_smoked_today)

        setContentView(R.layout.activity_smoking_home)
        val randomInt = (0..11).shuffled().last()
        bindSmokingDanger(smokingDangers[randomInt])

        smoked_today_increment_button = findViewById(R.id.smoking_today_card)
        smoking_stats_button = findViewById(R.id.smoking_stats_preview)
        smoked_today_textview = findViewById(R.id.cigs_smoked_today_num)
        craving_button = findViewById(R.id.craving_button)

        progress_bar_yesterday = findViewById(R.id.cigs_smoked_yesterday_bar)
        progress_bar_today= findViewById(R.id.cigs_smoked_today_bar)

        progress_bar_today.apply {
            progress = 0f
            progressMax = 20f
        }

        smoked_today_increment_button.setOnClickListener {
            cigs_smoked_today += 1.0f
            smoked_today_textview.text = cigs_smoked_today.toInt().toString()

            if (cigs_smoked_today == progress_bar_today.progressMax) {
                progress_bar_today.progressMax *= 2.0f
            }

            progress_bar_today.progress = cigs_smoked_today
            savedMoney(cigs_smoked_today)
        }

        smoking_stats_button.setOnClickListener {
            Intent(applicationContext, SmokingStatsActivity::class.java).also {
                startActivity(it)
            }
        }

        craving_button.setOnClickListener {
            Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")).also {
                startActivity(it)
            }
        }

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.smoking_menu_button ->{
                    showPopup(view)
                }
            }
        }
        smoking_menu_button.setOnClickListener(clickListener)

    }

    override fun onBackPressed() {

        if (progress_bar_today.progressMax > 20f && cigs_smoked_today <= progress_bar_today.progressMax/2) {
            progress_bar_today.progressMax /= 2.0f
        }

        if (cigs_smoked_today > 0.0f) cigs_smoked_today -= 1.0f
        smoked_today_textview.text = (cigs_smoked_today.toInt().toString())

        progress_bar_today.progress = cigs_smoked_today

    }

    private fun bindSmokingDanger(smokingDanger: SmokingDanger) {

        val dangersText = findViewById<TextView>(R.id.smoking_dangers_text)
        val dangersIcon = findViewById<ImageView>(R.id.smoking_dangers_icon)

        dangersText.text = smokingDanger.dangerText
        dangersIcon.setImageResource(smokingDanger.icon)
    }

    private fun showPopup(view: View){
        val popUp: PopupMenu?
        popUp = PopupMenu(this, view)
        popUp.inflate(R.menu.other_menu)

        popUp.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.header1 -> {
                    this.startActivity(Intent(this,HomePageActivity::class.java))
                }
                R.id.header2 -> {
                    Toast.makeText(this@SmokingHomeActivity, "Vreau sa deschid profileFragment", Toast.LENGTH_SHORT).show()//dar inca nu nu stiu cum...
                }
                R.id.header3 -> {
                    AlertDialog.Builder(this).apply {
                        setTitle("Are you sure?")
                        setPositiveButton("Yes"){_, _ ->
                            FirebaseAuth.getInstance().signOut()
                            flagsLogOut()
                        }
                        setNegativeButton("Cancel"){_, _ ->
                        }
                    }.create().show()
                }
            }

            true
        })
        popUp.show()
    }

    override fun onResume() {
        super.onResume()
        savedMoney(cigs_smoked_today)
    }

    override fun onStart() {
        super.onStart()
         savedMoney(cigs_smoked_today)
    }


    private fun savedMoney(cigs: Float){
        val db = FirebaseFirestore.getInstance()
        val uID = FirebaseAuth.getInstance().currentUser?.uid?:"Null UID"
        val cal = Calendar.getInstance()
        var currentDay = cal.get(Calendar.DAY_OF_YEAR)
        Log.d(TAG, "Ziua este $currentDay")
        var currentHour = cal.get(Calendar.HOUR)
        db.collection("users").document(uID).get()
            .addOnSuccessListener { result ->
                Log.d(TAG,"From Firestore with love: ${result["ID"]}")
                var money = result["SavedMoney"] as Double
                Log.d(TAG,"cIGARS SMOKED $cigs" )
                //trebuie rezolvata problema cu schimbarea zilei
                if(cigs == 0.0f){
                    if(currentDay > result["Start day"] as Long && currentHour >= result["Start hour"] as Long){
                        var cost = result["Cigs_Cost"] as String
                        if(cost == "")
                            cost = 0.toString()
                        money += (cost).toInt()
                        Log.d(TAG, "Cigs  e 0")
                    }
                }else{
                    var costCigs = (((result["Cigs_Cost"] as String).toDouble()) / 20.toDouble()) * cigs.toDouble()
                    money -= costCigs//in costCigs e pretul pentru tigarile fumate
                    Log.d(TAG, "Banii cheltuiti pe tigari ${costCigs.toString()} and $money")
                }
                val addOnFailureListener = db.collection("users").document(uID).update(
                    "Start day", currentDay,
                    "SavedMoney", money
                )
                    .addOnSuccessListener {
                        Log.w(TAG, "DocumentSnapshot updated with money: $money")

                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)

                    }
                smoking_money_saved.text = "Money saved:\n"+ money
            }
            .addOnFailureListener{
                Log.w(TAG, "Error getting the info")
            }
    }
}
