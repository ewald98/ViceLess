package com.evdev.viceless.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evdev.viceless.R
import com.evdev.viceless.activities.IntroSlide
import com.evdev.viceless.alcohol.AlcoholHomeActivity
import com.evdev.viceless.drugs.DrugsHomeActivity
import com.evdev.viceless.smoking.SmokingHomeActivity
import com.evdev.viceless.smoking.SmokingIntroActivity
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        smokingButton.setOnClickListener{
            activity?.let {
                val intent = Intent (it, SmokingIntroActivity::class.java)
                it.startActivity(intent)
            }
        }
        alcoholButton.setOnClickListener{
            activity?.let {
                val intent = Intent (it, AlcoholHomeActivity::class.java)
                it.startActivity(intent)
            }
        }
        drugButton.setOnClickListener{
            activity?.let {
                val intent = Intent (it, DrugsHomeActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

}
