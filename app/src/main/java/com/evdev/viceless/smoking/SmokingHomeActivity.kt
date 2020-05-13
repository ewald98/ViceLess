package com.evdev.viceless.smoking

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.evdev.viceless.R
import com.evdev.viceless.smoking.Supplier.smokingDangers
import com.mikhaellopez.circularprogressbar.CircularProgressBar


class SmokingHomeActivity : AppCompatActivity() {

    var cigs_smoked_today = 0.0f
    private lateinit var smoked_today_increment_button: RelativeLayout
    private lateinit var smoking_stats_button: LinearLayout
    private lateinit var smoked_today_textview: TextView

    private lateinit var progress_bar_today: CircularProgressBar
    private lateinit var progress_bar_yesterday: CircularProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smoking_home)

        val randomInt = (0..11).shuffled().last()
        bindSmokingDanger(smokingDangers[randomInt])

        smoked_today_increment_button = findViewById(R.id.smoking_today_card)
        smoking_stats_button = findViewById(R.id.smoking_stats_preview)
        smoked_today_textview = findViewById(R.id.cigs_smoked_today_num)

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
                progress_bar_today.progressMax *= 2.0f;
            }

            progress_bar_today.progress = cigs_smoked_today
        }

        smoking_stats_button.setOnClickListener {
            Intent(applicationContext, SmokingStatsActivity::class.java).also {
                startActivity(it)
            }
        }

    }

    override fun onBackPressed() {

        if (progress_bar_today.progressMax > 20f && cigs_smoked_today <= progress_bar_today.progressMax/2) {
            progress_bar_today.progressMax /= 2.0f;
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
}
