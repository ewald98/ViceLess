package com.evdev.viceless.smoking

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.activity_smoking_home.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


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
        savedMoney()
        setContentView(R.layout.activity_smoking_home)

//        Query query =
//        doc.get()
        val money_saved = 0

        val money_saved_tv = findViewById<TextView>(R.id.smoking_money_saved)
        money_saved_tv.text = "Money saved: \n$money_saved lei"

        val randomInt = (0 until Supplier.smokingDangers.size).shuffled().last()
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
        }

        smoking_stats_button.setOnClickListener {
            Intent(applicationContext, SmokingStatsActivity::class.java).also {
                startActivity(it)
            }
        }

        craving_button.setOnClickListener {

            val randomInt = (0 until Supplier.cravingLinks.size).shuffled().last()
            val uriString = Supplier.cravingLinks[randomInt]

//            val rightNow: Date = Date()
//            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//            val formatted = formatter.format(rightNow)
//
//            if (user != null) {
//                db.collection("users").document(user.uid)
//                    .update("cravings", FieldValue.arrayUnion(formatted))
//            }

            Intent(Intent.ACTION_VIEW, Uri.parse(uriString)).also {
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
}
