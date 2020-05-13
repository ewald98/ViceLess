package com.evdev.viceless.smoking

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.evdev.viceless.R
import com.evdev.viceless.smoking.Supplier.smokingDangers
import com.mikhaellopez.circularprogressbar.CircularProgressBar


class SmokingHomeActivity : AppCompatActivity() {

    var x = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smoking_home)

        onBackPressed()

        val randomInt = (0..11).shuffled().last()
        bindSmokingDanger(smokingDangers[randomInt])

        val inc: RelativeLayout = findViewById(R.id.smoking_today_card)
        val smoking_stats_button = findViewById<LinearLayout>(R.id.smoking_stats_preview)
        val tv: TextView = findViewById(R.id.cigs_smoked_today_num)

        val progress_bar_yesterday =
            findViewById<CircularProgressBar>(R.id.cigs_smoked_yesterday_bar)

        val progress_bar_today=
            findViewById<CircularProgressBar>(R.id.cigs_smoked_today_bar)

        progress_bar_today.apply {
            progress = 0f
            progressMax = 20f
        }

        inc.setOnClickListener {
            x += 1.0f
            tv.setText(x.toInt().toString())

            if (x == progress_bar_today.progressMax) {
                progress_bar_today.progressMax *= 2.0f;
            }

            progress_bar_today.progress = x
        }

        smoking_stats_button.setOnClickListener {
            Intent(applicationContext, SmokingStatsActivity::class.java).also {
                startActivity(it)
            }
        }


    }

    override fun onBackPressed() { }

    private fun bindSmokingDanger(smokingDanger: SmokingDanger) {

        val dangersText = findViewById<TextView>(R.id.smoking_dangers_text)
        val dangersIcon = findViewById<ImageView>(R.id.smoking_dangers_icon)

        dangersText.text = smokingDanger.dangerText
        dangersIcon.setImageResource(smokingDanger.icon)

    }
}
