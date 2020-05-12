package com.evdev.viceless

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.mikhaellopez.circularprogressbar.CircularProgressBar


class SmokingHomeActivity : AppCompatActivity() {

    var x = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smoking_home)

        // TODO: add onclick redirect to a url?
        val smokingDangers = listOf(
            SmokingDanger("Smoking can affect sperm quality and decreases fertility",
                R.drawable.smoking_danger_icon1),
            SmokingDanger("Smoking clogs the arteries and causes heart attacks and strokes",
                R.drawable.smoking_danger_icon2),
            SmokingDanger("Smoking has an indirect relation with hearing loss",
                R.drawable.smoking_danger_icon3),
            SmokingDanger("Smoking causes impotence in men",
                R.drawable.smoking_danger_icon4),
            SmokingDanger("Cigarette smoke contains benzene, nitrosamines, formaldehyde and hydrogen cyanide.",
                R.drawable.smoking_danger_icon5),
            SmokingDanger("Smoking when pregnant harms your baby",
                R.drawable.smoking_danger_icon6),
            SmokingDanger("Smoking causes tooth decay",
                R.drawable.smoking_danger_icon7),
            SmokingDanger("Smoking causes bad breath",
                R.drawable.smoking_danger_icon8),
            SmokingDanger("Smoking contributes to the development of arteriosclerosis or hardening of the arteries, that can contribute to or worsen vascular disease of the eyes",
                R.drawable.smoking_danger_icon9),
            SmokingDanger("Cigars Are Not A Safe Alternative To Cigarettes",
                R.drawable.smoking_danger_icon10),
            SmokingDanger("Smoking reduces blood circulation",
                R.drawable.smoking_danger_icon2),
            SmokingDanger("Cigarette smoke also harms those who don't smoke",
                R.drawable.smoking_danger_icon5),
            SmokingDanger("Smoking causes lung cancer",
                R.drawable.smoking_danger_icon10)
        )

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
