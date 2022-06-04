package com.example.dakt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.text.SimpleDateFormat

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
        changeValidElementsText(days.sum().toString())
        // todo: connect to char lib
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