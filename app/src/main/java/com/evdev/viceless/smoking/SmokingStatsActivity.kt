package com.evdev.viceless.smoking

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.evdev.viceless.R
import com.evdev.viceless.activities.HomePageActivity
import com.evdev.viceless.utils.flagsLogOut
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_smoking_stats.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SmokingStatsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smoking_stats)

        val cigsBarChart = findViewById<BarChart>(R.id.daily_cigs_bar_chart)
        val moneyBarChart = findViewById<BarChart>(R.id.daily_money_saved_bar_chart)
        val cigsRadarChart = findViewById<RadarChart>(R.id.cigs_radar_chart)
        val cravingRadarChart = findViewById<RadarChart>(R.id.craving_hours_radar_chart)

        createCharts(cigsBarChart, moneyBarChart, cigsRadarChart, cravingRadarChart)
        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.smoking_menu_button ->{
                    showPopup(view)
                }
            }
        }
        smoking_menu_button.setOnClickListener(clickListener)

    }

    private fun createCharts(cigsBarChart: BarChart, moneyBarChart: BarChart, cigsRadarChart: RadarChart, cravingsRadarChart: RadarChart) {

        val db = Firebase.firestore
        val user = FirebaseAuth.getInstance().currentUser

        val today: Date = Date()
        val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val formatted = formatter.format(today)

        if (user != null) {
            db.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { result ->

                    if (result.contains("cigs")) {
                        val cravingsArr = result["cravings"] as List<String>

                        val pack_cost = (result["Cigs_Cost"] as String).toFloat()
                        val avgCigsPerDay = (result["Cigs_Smoked"] as String).toFloat()

                        val money_spent_per_day_past = (pack_cost / 20) * avgCigsPerDay

                        var cigsArr = result["cigs"] as List<String>
//                        cigsArr = cigsArr.filter { s -> !s.contains(formatted) }

                        val start_date = result["start_date"] as String
                        val format_extracter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val start_date_parsed = format_extracter.parse(start_date)

                        val view_format: DateFormat = SimpleDateFormat("MM-dd")

                        var date = start_date_parsed
                        var i = 0
                        var cigs_count: ArrayList<Int> = ArrayList<Int>()
                        var day_dates: ArrayList<String> = ArrayList<String>()

                        while (date.before(today)) {

                            val date_formatted = view_format.format(date)
                            day_dates.add(date_formatted)
                            cigs_count.add((cigsArr.filter{s -> s.contains(date_formatted)}).size)

                            // date = tomorrow
                            date = date.getRelativeDay(1)
                            i += 1
                        }

                        var money_saved = cigs_count.map { cigs ->
                            money_spent_per_day_past - cigs * (pack_cost/20) }

                        createDailyCigsBarChart(cigsBarChart, day_dates, cigs_count, i)
                        createMoneySavedBarChart(moneyBarChart, day_dates,
                            money_saved as ArrayList<Float>, i)

                        // RADAR CHARTS
                        val cigHoursRaw = cigsArr.map { s -> s.substring(11, 13) }
                        val cigHours: ArrayList<Int> = ArrayList<Int>()

                        for (i in (0 until 24)) {
                            cigHours.add(cigHoursRaw.filter{ s -> s.equals(i.toString())}.size)
                        }
                        val avgCigHours = cigHours.map { count -> count.toFloat() / i }

                        createCigHoursRadarChart(cigsRadarChart, avgCigHours as ArrayList<Float>)

                        val cravingHoursRaw = cravingsArr.map { s -> s.substring(11, 13)}
                        val cravingHours: ArrayList<Int> = ArrayList<Int>()

                        for (i in (0 until 24)) {
                            cravingHours.add(cravingHoursRaw.filter{ s -> s.equals(i.toString())}.size)
                        }
                        val avgCravingHours = cravingHours.map { count -> count.toFloat() / i }

                        createCigHoursRadarChart(cravingsRadarChart, avgCravingHours as ArrayList<Float>)
                    }

                }
                .addOnFailureListener { exception ->
                    Log.w("error", "Error getting documents: ", exception)
                }
        }
    }

    private fun createCigHoursRadarChart(radarChart: RadarChart, avgCigHours: ArrayList<Float>) {

        val hours = arrayListOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23")
        val xAxisFormatter = IndexAxisValueFormatter(hours)

        val xAxis: XAxis = radarChart.getXAxis()
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day

        xAxis.labelCount = 7
        xAxis.valueFormatter = xAxisFormatter


        var entries = ArrayList<RadarEntry>()

        for (avgCigs in avgCigHours) {
            entries.add(RadarEntry(avgCigs))
        }

        val radarDataSet = RadarDataSet(entries, "craving hours")
        radarDataSet.setDrawValues(true)
        radarDataSet.color = Color.argb(255, 52, 75, 91)

        radarChart.legend.isEnabled = false

        val radarData = RadarData(radarDataSet)
        radarChart.setTouchEnabled(true)
        radarChart.data = radarData
        radarChart.description.text = ""
        radarChart.setNoDataText("No data available!")
        radarChart.setNoDataTextColor(Color.BLACK)

        radarChart.setTouchEnabled(true)

        radarChart.animateY(1000)
    }

    private fun createDailyCigsBarChart(barChart: BarChart, days: ArrayList<String>, cigs: ArrayList<Int>, count: Int) {

        val xAxisFormatter = IndexAxisValueFormatter(days)

        val xAxis: XAxis = barChart.getXAxis()
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day

        xAxis.labelCount = 7
        xAxis.valueFormatter = xAxisFormatter

        val rAxis = barChart.axisRight
        rAxis.axisMinimum = 0f

        val lAxis = barChart.axisLeft
        lAxis.axisMinimum = 0f


        val cigsSmoked = ArrayList<BarEntry>()
        val max_cigs = cigs.max()
        val colors = ArrayList<Int>()

        var i = 0f
        for (cig in cigs) {
            cigsSmoked.add(BarEntry(i, cig.toFloat()))
            colors.add(Color.argb(55 + (200 * (cig.toFloat()/ max_cigs!!)).toInt(), 52, 75, 91))
            i += 1f
        }

        val barDataSet = BarDataSet(cigsSmoked, "Daily smoked cigs")
        barDataSet.setDrawValues(true)

        barDataSet.colors = colors

        barChart.legend.isEnabled = false

        val barData = BarData(barDataSet)
        barChart.setTouchEnabled(true)
        barChart.data = barData
        barChart.description.text = ""
        barChart.setNoDataText("No data available!")
        barChart.setNoDataTextColor(Color.BLACK)
        barChart.setDrawGridBackground(false)
        barChart.setDrawBorders(false)
        barChart.setMaxVisibleValueCount(7)

        barChart.setTouchEnabled(true)
        barChart.isScaleXEnabled = true
        barChart.isScaleYEnabled = false

        barChart.animateY(1000)
    }

    private fun createMoneySavedBarChart(barChart: BarChart, days: ArrayList<String>, money : ArrayList<Float>, count: Int) {

        val xAxisFormatter = IndexAxisValueFormatter(days)

        val xAxis: XAxis = barChart.getXAxis()
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day

        xAxis.labelCount = 7
        xAxis.valueFormatter = xAxisFormatter


        val cigsSmoked = ArrayList<BarEntry>()
        val max_money = money.max()
        val colors = ArrayList<Int>()

        var i = 0f
        for (cig in money) {
            cigsSmoked.add(BarEntry(i, cig.toFloat()))
            if (cig > 0f)
                colors.add(Color.argb(55 + (200 * (cig.toFloat()/ max_money!!)).toInt(), 52, 75, 91))
            else
                colors.add(Color.argb(100 + (155 * (cig.toFloat()/ max_money!!)).toInt(), 247,80, 0))
            i += 1f
        }

        val barDataSet = BarDataSet(cigsSmoked, "Daily smoked cigs")
        barDataSet.setDrawValues(true)

        barDataSet.colors = colors

        barChart.legend.isEnabled = false

        val barData = BarData(barDataSet)
        barChart.setTouchEnabled(true)
        barChart.data = barData
        barChart.description.text = ""
        barChart.setNoDataText("No data available!")
        barChart.setNoDataTextColor(Color.BLACK)
        barChart.setDrawGridBackground(false)
        barChart.setDrawBorders(false)
        barChart.setMaxVisibleValueCount(7)

        barChart.setTouchEnabled(true)
        barChart.isScaleXEnabled = true
        barChart.isScaleYEnabled = false

        barChart.animateY(1000)
    }

    private fun showPopup(view: View){
        val popUp: PopupMenu?
        popUp = PopupMenu(this, view)
        popUp.inflate(R.menu.other_menu)

        popUp.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.header1 -> {
                    this.startActivity(Intent(this, HomePageActivity::class.java))
                }
                R.id.header2 -> {
                    Toast.makeText(this, "Vreau sa deschid profileFragment", Toast.LENGTH_SHORT).show()//dar inca nu nu stiu cum...
                }
                R.id.header3 -> {
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
            }

            true
        })
        popUp.show()
    }

}

private fun Date.getRelativeDay(day: Int): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.DATE, day)
    return cal.time
}