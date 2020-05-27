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
import com.evdev.viceless.utils.Supplier.cravingLinks
import com.evdev.viceless.utils.Supplier.smokingDangers
import com.evdev.viceless.utils.flagsLogOut
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
//    private lateinit var progress_bar_yesterday: CircularProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_smoking_home)
        val randomInt = (0 until smokingDangers.size).shuffled().last()
        bindSmokingDanger(smokingDangers[randomInt])

        smoked_today_increment_button = findViewById(R.id.smoking_today_card)
        smoking_stats_button = findViewById(R.id.smoking_stats_preview)
        smoked_today_textview = findViewById(R.id.cigs_smoked_today_num)
        craving_button = findViewById(R.id.craving_button)

//        progress_bar_yesterday = findViewById(R.id.cigs_smoked_yesterday_bar)
        progress_bar_today= findViewById(R.id.cigs_smoked_today_bar)

        val db = Firebase.firestore
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            db.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { result ->

                    val tv: TextView = findViewById<TextView>(R.id.stats_message)

                    val rightNow: Date = Date()
                    val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val formatted = formatter.format(rightNow)

                    var cigs_smoked_past = (result["Cigs_Smoked"] as String).toInt()


                    if (result.contains("cigs")) {
                        var cigsArr = result["cigs"] as List<String>
                        var cigsToday: List<String> = cigsArr.filter { s -> s.contains(formatted) }

                        cigs_smoked_today = cigsToday.size.toFloat()

                        // set progress bars
                        val day_before_today = mutableListOf<String>(
                            formatter.format(rightNow.getRelativeDay(-1)),
                            formatter.format(rightNow.getRelativeDay(-2)),
                            formatter.format(rightNow.getRelativeDay(-3)),
                            formatter.format(rightNow.getRelativeDay(-4))
                        )

                        val y_count = mutableListOf(0, 0, 0, 0)

                        for (cig in cigsArr)
                            for (i in (0 until 4))
                                if (cig.contains(day_before_today[i]))
                                    y_count[i] += 1

                        val y_max = y_count.max()
                        for (i in 0 until 4) {
                            y_count[i] = ((y_count[i].toFloat() / y_max!!) * 100f).toInt()
                        }

                        val pb1 = findViewById<ProgressBar>(R.id.progressBar1)
                        val pb2 = findViewById<ProgressBar>(R.id.progressBar2)
                        val pb3 = findViewById<ProgressBar>(R.id.progressBar3)
                        val pb4 = findViewById<ProgressBar>(R.id.progressBar4)

                        pb1.progress = y_count[0]
                        pb2.progress = y_count[1]
                        pb3.progress = y_count[2]
                        pb4.progress = y_count[3]

                        pb1.alpha = (y_count[0].toFloat() / 100)
                        pb2.alpha = (y_count[1].toFloat() / 100)
                        pb3.alpha = (y_count[2].toFloat() / 100)
                        pb4.alpha = (y_count[3].toFloat() / 100)

                        if (y_count[0] > y_count[1]) {
                            tv.text = "It's not over, \ndon't give up!"
                        } else {
                            tv.text = "You're making \nprogress!"
                        }
                    } else {
                        tv.text = "No stats yet!"
                    }

                    smoked_today_textview.text = cigs_smoked_today.toInt().toString()
                    progress_bar_today.apply {
                        progress = cigs_smoked_today
                        progressMax = cigs_smoked_past.toFloat() * (progress.toInt()/cigs_smoked_past + 1)
                    }


                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }

        }
        savedMoney(cigs_smoked_today)

        smoked_today_increment_button.setOnClickListener {

            //query add cig
            val db = Firebase.firestore
            val user = FirebaseAuth.getInstance().currentUser

            val rightNow: Date = Date()
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formatted = formatter.format(rightNow)

            if (user != null) {
                db.collection("users").document(user.uid)
                    .update("cigs", FieldValue.arrayUnion(formatted))
            }

            cigs_smoked_today += 1.0f
            smoked_today_textview.text = cigs_smoked_today.toInt().toString()

            if (cigs_smoked_today == progress_bar_today.progressMax) {
                progress_bar_today.progressMax *= 2.0f
            }

            progress_bar_today.progress = cigs_smoked_today
//            savedMoney(cigs_smoked_today)
        }

        smoking_stats_button.setOnClickListener {
            Intent(applicationContext, SmokingStatsActivity::class.java).also {
                startActivity(it)
            }
        }

        craving_button.setOnClickListener {

            val randomInt = (0 until cravingLinks.size).shuffled().last()
            val uriString = cravingLinks[randomInt]

            val db = Firebase.firestore
            val user = FirebaseAuth.getInstance().currentUser

            val rightNow: Date = Date()
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formatted = formatter.format(rightNow)

            if (user != null) {
                db.collection("users").document(user.uid)
                    .update("cravings", FieldValue.arrayUnion(formatted))
            }

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

//    override fun onBackPressed() {
//
//        if (progress_bar_today.progressMax > 20f && cigs_smoked_today <= progress_bar_today.progressMax/2) {
//            progress_bar_today.progressMax /= 2.0f
//        }
//
//        if (cigs_smoked_today > 0.0f) cigs_smoked_today -= 1.0f
//        smoked_today_textview.text = (cigs_smoked_today.toInt().toString())
//
//        progress_bar_today.progress = cigs_smoked_today
//
//    }

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
//        savedMoney(cigs_smoked_today)
    }

    override fun onStart() {
        super.onStart()
//         savedMoney(cigs_smoked_today)
    }


    private fun savedMoney(cigs: Float){
        val db = FirebaseFirestore.getInstance()
        val uID = FirebaseAuth.getInstance().currentUser?.uid?:"Null UID"

        db.collection("users").document(uID).get()
            .addOnSuccessListener { result ->
                if(result.contains("cigs")) {
                    var cigsArr = result["cigs"] as List<String>
                    var cigs_smoked_past = (result["Cigs_Smoked"] as String).toDouble()
                    var cig_cost = (result["Cigs_Cost"] as String).toDouble() / 20
                    Log.d("cigsArr", cigsArr.size.toString())

                    var start_date = result["start_date"] as String
                    val format_extracter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val start_date_parsed = format_extracter.parse(start_date)
                    val rightNow: Date = Date()

                    val diff: Long = rightNow.time - start_date_parsed.time
                    val days_since_start = diff / (1000*60*60*24)
                    Log.d("diferenta", days_since_start.toString())

                    var money_saved = days_since_start * cigs_smoked_past * cig_cost - (cigsArr.size * cig_cost)
                    smoking_money_saved.text = "Money saved:\n"+ money_saved + " lei"

                }
                Log.d("cigsArr", "couldn't be read")
            }
            .addOnFailureListener{
                Log.w(TAG, "Error getting the info")
            }
    }
}

private fun Date.getRelativeDay(day: Int): Date {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DATE, day)
    return cal.time
}
