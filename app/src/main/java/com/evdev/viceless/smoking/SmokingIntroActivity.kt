package com.evdev.viceless.smoking

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.evdev.viceless.R
import com.evdev.viceless.activities.IntroSlide
import com.evdev.viceless.activities.IntroSliderAdapter
import kotlinx.android.synthetic.main.activity_smoking_intro.*


class SmokingIntroActivity : AppCompatActivity() {

    private var startDate = ""
    private val introSliderAdapter =
        IntroSliderAdapter(
            listOf(
                IntroSlide(
                    "How many cigarettes do you usually smoke a day?",
                    R.drawable.intro_image1
                ),
                IntroSlide(
                    "How much does a pack cost on average?",
                    R.drawable.intro_image2
                ),
                IntroSlide(
                    "For how long have you been a smoker?",
                    R.drawable.intro_image3
                )
            )
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideActionBar()

        setContentView(R.layout.activity_smoking_intro)
        introSliderViewPager.adapter = introSliderAdapter

        startIndicators()

        setupButtons()
    }

    private fun hideActionBar() {
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {}
    }

    private fun setupButtons() {
        buttonNext.setOnClickListener {
            if (introSliderViewPager.currentItem + 1 < introSliderAdapter.itemCount) {
                introSliderViewPager.currentItem += 1
            } else {
                startDate= System.currentTimeMillis().toString()
                goToNextActivity()
            }
        }

        skip_button.setOnClickListener {
            goToNextActivity()
        }
    }

    private fun startIndicators() {
        setupIndicators()
        setCurrentIndicator(0)
        introSliderViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        }
        )
    }

    //string saves data from questions
    private fun goToNextActivity() {
        val s: Array<String> = introSliderAdapter.retrieveData()
        Intent(applicationContext, SmokingHomeActivity::class.java).also {
            it.putExtra("Answers",s)
            it.putExtra("StartDate",startDate)
            startActivity(it)
        }
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            indicatorsContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = indicatorsContainer.childCount

        for (i in 0 until childCount) {
            val imageView = indicatorsContainer[i] as ImageView
            imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    if (i == index) R.drawable.indicator_active else R.drawable.indicator_inactive
                )
            )
        }
    }
}

