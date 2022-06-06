package com.example.dakt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import kotlin.math.floor

class StatisticsView : AppCompatActivity() {
    private val model: MVCModel = MVCModel(this)
    private lateinit var statEdFrom: EditText
    private lateinit var statEdTo: EditText
    private lateinit var statView: LinearLayout
    private lateinit var statValidElements: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_view)

        statEdFrom = findViewById(R.id.statEdFrom)
        statEdTo = findViewById(R.id.statEdTo)
        statView = findViewById(R.id.statView)
        statValidElements = findViewById(R.id.statValidElements)

        findViewById<Button>(R.id.switchBackFromStatistics).setOnClickListener {
            finish()
        }
        findViewById<ImageButton>(R.id.statConfirmButton).setOnClickListener { generateDaysOfWeekStatClick() }
    }
    // Start of >>>>VIEW<<<< functions
    private fun statMakeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun changeValidElementsText(str: String) {
        statValidElements.text = "Valid elements: $str"
    }
    private fun generateDaysOfWeekStat(fromT: Long, toT: Long) {
        val days: IntArray = model.daysOfWeek(fromT, toT)
        val sumDays = days.sum()
        changeValidElementsText(sumDays.toString())

        val dayNames: List<String> = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        // todo: connect to char lib
        statView.removeAllViews()

        val chart: BarChart = BarChart(this)
        val layParam: FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        chart.layoutParams = layParam
        chart.legend.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.setFitBars(true)
        chart.description.isEnabled = false
        val xAxis: XAxis = chart.xAxis
        xAxis.isEnabled = true
        xAxis.setDrawLabels(true)
        xAxis.valueFormatter = IndexAxisValueFormatter(dayNames)
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM


        val yAxis: YAxis = chart.axisLeft
        yAxis.granularity = 1f
        yAxis.axisMinimum = 0f
        val max = days.maxOrNull()!!
        yAxis.axisMaximum = floor(max.toFloat() / sumDays * 100) + (max.toFloat() / sumDays * 100 / 10)


        val entries: MutableList<MutableList<BarEntry>> = mutableListOf()
        entries.add(mutableListOf(BarEntry(0F, days[0].toFloat() / sumDays * 100)))
        entries.add(mutableListOf(BarEntry(1F, days[1].toFloat() / sumDays * 100)))
        entries.add(mutableListOf(BarEntry(2F, days[2].toFloat() / sumDays * 100)))
        entries.add(mutableListOf(BarEntry(3F, days[3].toFloat() / sumDays * 100)))
        entries.add(mutableListOf(BarEntry(4F, days[4].toFloat() / sumDays * 100)))
        entries.add(mutableListOf(BarEntry(5F, days[5].toFloat() / sumDays * 100)))
        entries.add(mutableListOf(BarEntry(6F, days[6].toFloat() / sumDays * 100)))

        val dataSets: MutableList<BarDataSet> = mutableListOf()
        dataSets.add(BarDataSet(entries[0], null) )
        dataSets[0].color = resources.getColor(R.color.cosmic_cobalt, this.theme)
        dataSets.add(BarDataSet(entries[1], null) )
        dataSets[1].color = resources.getColor(R.color.bottle_green, this.theme)
        dataSets.add(BarDataSet(entries[2], null) )
        dataSets[2].color = resources.getColor(R.color.mango, this.theme)
        dataSets.add(BarDataSet(entries[3], null) )
        dataSets[3].color = resources.getColor(R.color.Lava, this.theme)
        dataSets.add(BarDataSet(entries[4], null) )
        dataSets[4].color = resources.getColor(R.color.orchid_crayola, this.theme)
        dataSets.add(BarDataSet(entries[5], null) )
        dataSets[5].color = resources.getColor(R.color.tufts_blue, this.theme)
        dataSets.add(BarDataSet(entries[6], null) )
        dataSets[6].color = resources.getColor(R.color.medium_sea_green, this.theme)

        val barData: BarData = BarData(dataSets[0], dataSets[1], dataSets[2], dataSets[3], dataSets[4], dataSets[5], dataSets[6])
        barData.barWidth = 0.9f
//        barData.setValueFormatter(ValueFormatter)
        chart.data = barData
        chart.invalidate()      // refresh


        statView.addView(chart)
    }
    // End of >>>>VIEW<<<< functions
    // Start of >>>>CONTROLLER<<<< functions
    private fun generateDaysOfWeekStatClick() {
        val fromStr: String = statEdFrom.text.toString()
        val toStr: String = statEdTo.text.toString()
        changeValidElementsText("0")
        if (!DataValidator.checkIfValidDate(fromStr)) {
            statMakeToast("wrong start time!")
            return
        }
        if (!DataValidator.checkIfValidDate(toStr) ||
            !DataValidator.checkIfValidStartFinishDates(fromStr, toStr)) {
            statMakeToast("wrong end time!")
            return
        }
        val dateForm: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val fromT: Long = dateForm.parse(fromStr).time
        val toT: Long = dateForm.parse(toStr).time
        model.handleActivitiesStatistics(fromT, toT)
        generateDaysOfWeekStat(fromT, toT)
    }
    // End of >>>>CONTROLLER<<<< functions
}