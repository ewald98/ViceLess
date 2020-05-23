package com.evdev.viceless.smoking

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.evdev.viceless.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


class SmokingStatsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smoking_stats)

        val cigsBarChart = findViewById<BarChart>(R.id.daily_cigs_bar_chart)
        val moneyBarChart = findViewById<BarChart>(R.id.daily_money_saved_bar_chart)
        val cravingRadarChart = findViewById<RadarChart>(R.id.craving_hours_radar_chart)

        createBarChart(cigsBarChart)
        createBarChart(moneyBarChart)
        createRadarChart(cravingRadarChart)

    }

    private fun createRadarChart(radarChart: RadarChart) {

        val days = arrayListOf<String>("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23")
        val xAxisFormatter = IndexAxisValueFormatter(days)

        val xAxis: XAxis = radarChart.getXAxis()
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day

        xAxis.labelCount = 7
        xAxis.valueFormatter = xAxisFormatter


        var entries = ArrayList<RadarEntry>()
        entries.add(RadarEntry(4f))
        entries.add(RadarEntry(0f))
        entries.add(RadarEntry(1f))
        entries.add(RadarEntry(0f))
        entries.add(RadarEntry(0f))
        entries.add(RadarEntry(0f))
        entries.add(RadarEntry(0f))
        entries.add(RadarEntry(0f))
        entries.add(RadarEntry(0f))
        entries.add(RadarEntry(2f))
        entries.add(RadarEntry(2f))
        entries.add(RadarEntry(2f))
        entries.add(RadarEntry(1f))
        entries.add(RadarEntry(1.5f))
        entries.add(RadarEntry(2f))
        entries.add(RadarEntry(3f))
        entries.add(RadarEntry(1f))
        entries.add(RadarEntry(2f))
        entries.add(RadarEntry(3f))
        entries.add(RadarEntry(4f))
        entries.add(RadarEntry(5f))
        entries.add(RadarEntry(4f))
        entries.add(RadarEntry(3f))
        entries.add(RadarEntry(2f))


        val radarDataSet = RadarDataSet(entries, "craving hours")
        radarDataSet.setDrawValues(true)
        radarDataSet.color = Color.argb(255, 52, 75, 91)    // alpha 64 should be the minimum (... either 1 cig or the historical minimum)
//        barDataSet.resetColors()

//        barDataSet.setGradientColor(R.color.colorPrimary, R.color.colorSecondaryText)

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

    private fun createBarChart(barChart: BarChart) {


        val days = arrayListOf<String>("11.05", "12.05", "13.05", "14.05", "15.05", "16.05", "17.05", "18.05", "19.05")
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


        var cigsSmoked = ArrayList<BarEntry>()
        cigsSmoked.add(BarEntry(0f, 13f))
        cigsSmoked.add(BarEntry(1f, 14f))
        cigsSmoked.add(BarEntry(2f, 15f))
        cigsSmoked.add(BarEntry(3f, 16f))
        cigsSmoked.add(BarEntry(4f, 17f))
        cigsSmoked.add(BarEntry(5f, 18f))
        cigsSmoked.add(BarEntry(6f, 19f))
        cigsSmoked.add(BarEntry(7f, 20f))
        cigsSmoked.add(BarEntry(8f, 15f))

        val barDataSet = BarDataSet(cigsSmoked, "Daily smoked cigs")
        barDataSet.setDrawValues(true)
//        barDataSet.resetColors()

        barDataSet.colors = arrayListOf(
            Color.argb((255 * (5f/20)).toInt(), 52, 75, 91),    // alpha 64 should be the minimum (... either 1 cig or the historical minimum)
            Color.argb((255 * (14f/20)).toInt(), 52, 75, 91),   // alpha 255 should be the historical maximum
            Color.argb((255 * (15f/20)).toInt(), 52, 75, 91),
            Color.argb((255 * (16f/20)).toInt(), 52, 75, 91),
            Color.argb((255 * (17f/20)).toInt(), 52, 75, 91),
            Color.argb((255 * (18f/20)).toInt(), 52, 75, 91),
            Color.argb((255 * (19f/20)).toInt(), 52, 75, 91),
            Color.argb((255 * (20f/20)).toInt(), 52, 75, 91),
            Color.argb((255 * (15f/20)).toInt(), 52, 75, 91)
        )
//        barDataSet.setGradientColor(R.color.colorPrimary, R.color.colorSecondaryText)

        barChart.legend.isEnabled = false


        val barData = BarData(barDataSet)
        barChart.setTouchEnabled(true)
        barChart.data = barData
        barChart.description.text = ""
        barChart.setNoDataText("No data available!")
        barChart.setNoDataTextColor(Color.BLACK)
        barChart.setDrawGridBackground(false)
        barChart.setDrawBorders(false)
        barChart.setMaxVisibleValueCount(7);

        barChart.setTouchEnabled(true)
        barChart.isScaleXEnabled = true
        barChart.isScaleYEnabled = false

        barChart.animateY(1000)

    }

}