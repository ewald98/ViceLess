package com.evdev.viceless

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.evdev.viceless.Supplier.smokingDangers
import com.mikhaellopez.circularprogressbar.CircularProgressBar


class SmokingHomeActivity : AppCompatActivity() {

    var x = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smoking_home)


        val randomInt = (0..11).shuffled().last()
        bindSmokingDanger(smokingDangers[randomInt])

        val inc: RelativeLayout = findViewById(R.id.smoking_today_card)
        val tv: TextView = findViewById(R.id.cigs_smoked_today_num)

        val progress_bar_yesterday =
            findViewById<CircularProgressBar>(R.id.cigs_smoked_yesterday_bar)

        val progress_bar_today=
            findViewById<CircularProgressBar>(R.id.cigs_smoked_today_bar)

        progress_bar_today.apply {
            progress = 0f
            progressMax = 20f
        }

        inc.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                x += 1.0f
                tv.setText(x.toString())

                if (x == progress_bar_today.progressMax) {
                    progress_bar_today.progressMax *= 2.0f;
                }

                progress_bar_today.progress = x
            }
        })



    }

    private fun bindSmokingDanger(smokingDanger: SmokingDanger) {

        val dangersText = findViewById<TextView>(R.id.smoking_dangers_text)
        val dangersIcon = findViewById<ImageView>(R.id.smoking_dangers_icon)

        dangersText.text = smokingDanger.dangerText
        dangersIcon.setImageResource(smokingDanger.icon)

    }
}
