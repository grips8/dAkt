package com.example.dakt

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val model: MVCModel = MVCModel(this)
    private lateinit var edCategory: EditText
    private lateinit var edStarted: EditText
    private lateinit var edFinished: EditText
    private lateinit var edNotes: EditText
    private lateinit var horDate: EditText
    private lateinit var insBtn: ImageButton
    private lateinit var editButton: ImageButton
    private lateinit var deleteButton: ImageButton
    private lateinit var horLayout: RelativeLayout
    private val pixelRatio: Int = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edCategory = findViewById(R.id.edCategory)
        edStarted = findViewById(R.id.edStarted)
        edFinished = findViewById(R.id.edFinished)
        edNotes = findViewById(R.id.edNotes)
        horDate = findViewById(R.id.horDate)
        insBtn = findViewById(R.id.enableAddButton)
        editButton = findViewById(R.id.editButton)
        deleteButton = findViewById(R.id.deleteButton)
        horLayout = findViewById(R.id.horLayout)

        horLayout.minimumWidth = 24*60*pixelRatio
        horLayout.setBackgroundResource(R.drawable.hbg)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)

        horDate.setText("$day/$month/$year")
        changeDateClick(horDate.text.toString())

        findViewById<ImageButton>(R.id.dateButton).setOnClickListener {
            changeDateClick(horDate.text.toString())
        }

        findViewById<Button>(R.id.switchCatButton).setOnClickListener {
            val intent = Intent(this, CategoriesView::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.switchStatButton).setOnClickListener {
            val intent = Intent(this, StatisticsView::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.deleteButton).setOnClickListener { deleteClick() }
        findViewById<ImageButton>(R.id.enableAddButton).setOnClickListener { enableInsertClick() }
        findViewById<ImageButton>(R.id.prevDateButton).setOnClickListener { prevDayClick() }
        findViewById<ImageButton>(R.id.nextDateButton).setOnClickListener { nextDayClick() }
    }

// Start of >>>>VIEW<<<< functions
    private fun clearEdits() {
        edCategory.text = null
        edStarted.text = null
        edFinished.text = null
        edNotes.text = null
    }
    private fun switchEdits(enabled: Boolean) {     // true -> edits enabled; false -> edits disabled
        edCategory.isEnabled = enabled
        edStarted.isEnabled = enabled
        edFinished.isEnabled = enabled
        edNotes.isEnabled = enabled
    }
    private fun switchInsertButton(mode: Boolean) {    // true -> insertConfirmButton func.; false -> InsertButton func.
        if (mode) {
            insBtn.setOnClickListener { confirmInsertClick() }
            insBtn.setImageResource(R.drawable.tick_icon)
        }
        else {
            insBtn.setOnClickListener { enableInsertClick() }
            insBtn.setImageResource(R.drawable.add_icon)
        }
    }
    private fun switchEditButton(mode: Boolean) {       // true -> editConfirmation func.; false -> editButton func.
        if (mode) {
            editButton.setOnClickListener { confirmUpdateClick() }
            editButton.setImageResource(R.drawable.tick_icon)
        }
        else {
            editButton.setOnClickListener { enableUpdateClick() }
            editButton.setImageResource(R.drawable.edit_icon)
        }
    }
    private fun enableEditButton(enabled: Boolean) {      // true -> enable button; false -> disable button
        if (enabled) {
            editButton.imageAlpha = 255
            editButton.isEnabled = true
            editButton.background.setTint(Color.parseColor("#75E6DA")) // R.color.blue_grotto
        }
        else {
            editButton.imageAlpha = 75
            editButton.isEnabled = false
            editButton.background.setTint(Color.LTGRAY)
        }
    }
    private fun enableDeleteButton(enabled: Boolean) {      // true -> enable button; false -> disable button
        if (enabled) {
            deleteButton.imageAlpha = 255
            deleteButton.isEnabled = true
            deleteButton.background.setTint(Color.parseColor("#FF5C5C"))   // R.color.reddish
        }
        else {
            deleteButton.imageAlpha = 75
            deleteButton.isEnabled = false
            deleteButton.background.setTint(Color.LTGRAY)
        }
    }
    private fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun generateActivities(fromTime: Long) {
        val actButtons: MutableList<Button> = mutableListOf()
        var marg: Int
        var end: Int
        var layParam: FrameLayout.LayoutParams

        enableEditButton(false)
        enableDeleteButton(false)
        switchEdits(false)
        clearEdits()
        switchInsertButton(false)
        switchEditButton(false)

        val activities: MutableMap<Int, Activity> = model.getActivities()

        horLayout.removeAllViews()
        for ((key, value) in activities) {
            actButtons.add(Button(this))
            actButtons.last().id = value.id
            if (value.finished.time > fromTime + 86400000)
                end = 86400000
            else
                end = (value.finished.time - fromTime).toInt()
            if (value.started.time <= fromTime)
                marg = 0
            else
                marg = (value.started.time - fromTime).toInt()
            layParam = FrameLayout.LayoutParams((end - marg) / 60 / 1000 * pixelRatio, FrameLayout.LayoutParams.MATCH_PARENT - 10) // ????? xD
            layParam.setMargins(marg / 60 / 1000 * pixelRatio, 0, 0, 0)
            actButtons.last().layoutParams = layParam
            actButtons.last().text = value.name
            actButtons.last().setBackgroundColor(Color.DKGRAY)
            actButtons.last().isClickable = true
            val index = actButtons.last().id
            actButtons.last().setOnClickListener { selectClick(index) }
            actButtons.last().setBackgroundResource(R.drawable.actborder)

            horLayout.addView(actButtons.last())
        }
    }
    private fun generateSelected(mId: Int) {
        val act: Activity = model.getActivity(mId)
        enableEditButton(true)
        enableDeleteButton(true)
        switchEdits(false)
        switchInsertButton(false)
        switchEditButton(false)

        val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy kk:mm")
        edCategory.setText(act.name)
        edStarted.setText(dateFormat.format(act.started).toString())
        edFinished.setText(dateFormat.format(act.finished).toString())
        edNotes.setText(act.notes)
    }
    private fun changeHorDate(str: String) {
        horDate.setText(str)
    }
    private fun enabledInsert() {
        enableEditButton(false)
        enableDeleteButton(false)
        switchInsertButton(false)
        switchEdits(true)
        clearEdits()
        switchInsertButton(true)
    }
// End of >>>>VIEW<<<< functions

// Start of >>>>CONTROLLER<<<< functions
    private fun changeDateClick(strDate: String) {
        if (DataValidator.checkIfValidDate(strDate)) {
            val time: Long = SimpleDateFormat("dd/MM/yyyy").parse(strDate).time
            model.changeSelectedView(-1)
            model.handleGet(time)
            generateActivities(time)
        }
        else
            makeToast("wrong date!")
    }
    private fun prevDayClick() {
        val time: Long = model.getCurrDate() - 86400000
        // val time: Long = SimpleDateFormat("dd/MM/yyyy").parse(strDate).time - 86400000
        if (time < 0L)       // case when current date is 01/01/1970
            makeToast("Cannot go further!")
        else {
            model.changeSelectedView(-1)
            model.handleGet(time)
            generateActivities(time)
            changeHorDate(SimpleDateFormat("dd/MM/yyyy").format(Date(time)).toString())
        }
    }
    fun nextDayClick() {
        val time: Long = model.getCurrDate() + 86400000
        // val time: Long = SimpleDateFormat("dd/MM/yyyy").parse(strDate).time - 86400000
        if (time > 32503676400000L)       // case when current date is 01/01/1970
            makeToast("Cannot go further!")
        else {
            model.changeSelectedView(-1)
            model.handleGet(time)
            generateActivities(time)
            changeHorDate(SimpleDateFormat("dd/MM/yyyy").format(Date(time)).toString())
        }
    }
    private fun selectClick(mId: Int) {
        model.changeSelectedView(mId)
        generateSelected(mId)
    }
    private fun enableInsertClick() {
        model.changeSelectedView(-1)
        enabledInsert()
        switchInsertButton(true)
    }
    private fun confirmInsertClick() {
        if (!DataValidator.checkIfValidDateAndTime(edStarted.text.toString())) {
            makeToast("wrong start time!")
            return
        }
        if (!DataValidator.checkIfValidDateAndTime(edFinished.text.toString()) ||
            !DataValidator.checkIfValidStartFinishDates(edStarted.text.toString(), edFinished.text.toString())) {
            makeToast("wrong end time!")
            return
        }
        val catId: Int = model.fetchCategory(edCategory.text.toString())
        if (catId == -1) {
            makeToast("no such category!")
            return
        }
        model.handleInsert(catId, edStarted.text.toString(), edFinished.text.toString(), edNotes.text.toString())
        switchInsertButton(false)
        changeDateClick(edStarted.text.toString().split(' ')[0])
    }
    private fun enableUpdateClick() {
        switchEdits(true)
        switchEditButton(true)
    }
    private fun confirmUpdateClick() {
        if (!DataValidator.checkIfValidDateAndTime(edStarted.text.toString())) {
            makeToast("wrong start time!")
            return
        }
        if (!DataValidator.checkIfValidDateAndTime(edFinished.text.toString()) ||
            !DataValidator.checkIfValidStartFinishDates(edStarted.text.toString(), edFinished.text.toString())) {
            makeToast("wrong end time!")
            return
        }
        val catId: Int = model.fetchCategory(edCategory.text.toString())
        if (catId == -1) {
            makeToast("no such category!")
            return
        }
        model.handleUpdate(catId, edStarted.text.toString(), edFinished.text.toString(), edNotes.text.toString())
        switchEditButton(false)
        changeDateClick(edStarted.text.toString().split(' ')[0])
    }
    private fun deleteClick() {
        model.handleDelete()
        model.changeSelectedView(-1)
        model.handleGet(model.getCurrDate())
        generateActivities(model.getCurrDate())
    }
// Start of >>>>CONTROLLER<<<< functions
}