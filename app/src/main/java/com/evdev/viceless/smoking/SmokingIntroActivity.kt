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
import com.evdev.viceless.IntroSlide
import com.evdev.viceless.IntroSliderAdapter
import com.evdev.viceless.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_smoking_intro.*


class SmokingIntroActivity : AppCompatActivity() {

    private val introSliderAdapter = IntroSliderAdapter(Supplier.smokingIntroSlides)

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

    private fun goToNextActivity() {
        val s: Array<String> = introSliderAdapter.retrieveData()
//        val userInfo = hashMapOf(
//            "avgCigsPerDay" to s[0],
//            "avgPackCost" to s[1],
//            "yearsBeingSmoker" to s[2]
//        )
//
//        if (user != null) {
//            db.collection("users").document(user.uid)
//                .set(userInfo as Map<String, Any>, SetOptions.merge())
//        }

        Intent(applicationContext, SmokingHomeActivity::class.java).also {
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

