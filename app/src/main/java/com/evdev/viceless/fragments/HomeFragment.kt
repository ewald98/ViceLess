package com.evdev.viceless.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evdev.viceless.R
import com.evdev.viceless.activities.MainActivity
import com.evdev.viceless.activities.SmokingHomeActivity
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
                val intent = Intent (it, SmokingHomeActivity::class.java)
                it.startActivity(intent)
            }
        }
        alcoholButton.setOnClickListener{
            activity?.let {
                val intent = Intent (it, MainActivity::class.java)
                it.startActivity(intent)
            }
        }
        drugButton.setOnClickListener{
            activity?.let {
                val intent = Intent (it, MainActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

}
