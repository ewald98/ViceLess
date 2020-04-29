package com.evdev.viceless

import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Layout
import android.text.TextWatcher
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SmokingHomeActivity : AppCompatActivity() {

    var x = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smoking_home)

//        val progressBar : ProgressBar = findViewById(R.id.cigs_smoked_today_stat)
//        val animation = ObjectAnimator.ofInt( progressBar, "progress", 0, 20)
//        // see this max value coming back here, we animate towards that value
//
//        animation.duration = 5000 // in milliseconds
//
//        animation.interpolator = DecelerateInterpolator()
//        animation.start()
//
//        progressBar.clearAnimation();

        val inc: RelativeLayout = findViewById(R.id.smoking_today_card)
        val tv: TextView = findViewById(R.id.cigs_smoked_today_num)

        inc.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                x += 1
                tv.setText(x.toString())
            }
        })


    }
}
