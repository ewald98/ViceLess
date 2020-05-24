package com.evdev.viceless.smoking

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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


    private fun goToNextActivity() {
        var s: Array<String> = introSliderAdapter.retrieveData()
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: "Null UID"
        val email = FirebaseAuth.getInstance().currentUser?.email?:"No email"
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val startDate = System.currentTimeMillis()
        val user = User(uid, email, s[0], s[1], s[2],startDate,0)
        Log.d("Data in User object", "Answer 1 "+ user.cigs_smoked + " Answer 2 "+ user.cigs_cost + " Answer 3 "+ user.smoke_time + " Time:" + startDate)
        ref.setValue(user)
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
    class User(val uid: String = "", val username: String = "", val cigs_smoked: String = "", val cigs_cost: String = "", val smoke_time: String = "", val startDate: Long = 0,val moneySaved: Int = 0)
}





