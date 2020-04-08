package com.example.viceless

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlide("How many cigarettes do you usually smoke a day?", R.drawable.intro_image1),
            IntroSlide("How much does a pack cost on average?", R.drawable.intro_image2),
            IntroSlide("For how long have you been a smoker?", R.drawable.intro_image3)
            )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {}

        setContentView(R.layout.activity_main)
        introSliderViewPager.adapter = introSliderAdapter
    }
}
