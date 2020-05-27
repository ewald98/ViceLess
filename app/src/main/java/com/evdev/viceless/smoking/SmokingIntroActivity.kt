package com.evdev.viceless.smoking

import android.content.ContentValues.TAG
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
import com.evdev.viceless.utils.Supplier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_smoking_intro.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class SmokingIntroActivity : AppCompatActivity() {

    private var startDate = ""
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

    private fun goToNextActivity(){
        val cal = Calendar.getInstance()
//        var day = cal.get(Calendar.DAY_OF_YEAR)
//        var hour = cal.get(Calendar.HOUR)
        val rightNow: Date = Date()
        val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val formatted = formatter.format(rightNow)

        val db = FirebaseFirestore.getInstance()
        val uID = FirebaseAuth.getInstance().currentUser?.uid?:"Null UID"
        val email = FirebaseAuth.getInstance().currentUser?.email?:"No email"
        val s: Array<String> = introSliderAdapter.retrieveData()
        val user = hashMapOf(
            "ID" to uID,
            "email" to email,
            "Cigs_Smoked" to s[0],
            "Cigs_Cost" to s[1],
            "Smoking_Time" to s[2],
            "start_date" to formatted,
            "ChosenVice" to "Smoke",
            "SavedMoney" to 0.0
        )
        Log.w(TAG,"Datele: $user")
        db.collection("users").document(uID).set(user)
            .addOnSuccessListener {
                Log.w(TAG,"DocumentSnapshot added with ID:$uID")
            }
            .addOnFailureListener{e ->
                Log.w(TAG,"Error adding document",e)

            }
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





