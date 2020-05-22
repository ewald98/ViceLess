package com.evdev.viceless.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.evdev.viceless.R
import com.evdev.viceless.utils.flagsLogOut
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home_page.*

class HomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

//        //First we bind the variables to the buttons ids
//        //We use val instead of var because we want them to be "read-only"
//        val btnSmoke : ImageButton = findViewById(R.id.smokingButton)
//        val btnAlcohol : ImageButton = findViewById(R.id.alcoholButton)
//        val btnDrugs : ImageButton = findViewById(R.id.drugButton)
//
//        //It makes the buttons responsive on touch and open a new activity
//        btnSmoke.setOnClickListener {
//            val smoke = Intent(this, SmokingIntroActivity :: class.java)
//            startActivity(smoke)
//        }
//        btnAlcohol.setOnClickListener {
//            val alcohol = Intent(this, MainActivity :: class.java)
//            startActivity(alcohol)
//        }
//        btnDrugs.setOnClickListener {
//            val drugs = Intent(this, MainActivity :: class.java)
//            startActivity(drugs)
//        }
        setSupportActionBar(toolbar)

        val navController = Navigation.findNavController(this,
            R.id.fragment
        )
        NavigationUI.setupWithNavController(nav_view, navController)
        NavigationUI.setupActionBarWithNavController(this,navController, drawer_layout)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.fragment),drawer_layout)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId == R.id.action_logout){
            AlertDialog.Builder(this).apply {
                setTitle("Are you sure?")
                setPositiveButton("Yes"){_, _ ->
                    FirebaseAuth.getInstance().signOut()
                    flagsLogOut()
                }
                setNegativeButton("Cancel"){_, _ ->
                }
            }.create().show()
        }

        return super.onOptionsItemSelected(item)
    }

}
