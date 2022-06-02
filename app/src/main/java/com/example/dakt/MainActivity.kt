package com.example.dakt

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var dbHelper: DatabaseHelper = DatabaseHelper(this)
    private val activities: MutableMap<Int, Activity> = mutableMapOf()
    private val actButtons: MutableList<Button> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dateText: EditText = findViewById(R.id.horDate)
        dateText.setText("$day/$month/$year")
        loadActivities(dateText.text.toString())

        findViewById<ImageButton>(R.id.dateButton).setOnClickListener {
            loadActivities(dateText.text.toString())
        }

        findViewById<Button>(R.id.switchCatButton).setOnClickListener {
            val intent = Intent(this, CategoriesView::class.java)
            startActivity(intent)
        }
//        findViewById<ImageButton>(R.id.deleteButton).setOnClickListener {
//            handleDelete()
//            loadActivities(findViewById<EditText>(R.id.horDate).text.toString())
//        }
//
//        findViewById<ImageButton>(R.id.enableAddButton).setOnClickListener { enableInsert() }
//
//        findViewById<ImageButton>(R.id.prevDateButton).setOnClickListener { prevDay() }
//        findViewById<ImageButton>(R.id.nextDateButton).setOnClickListener { nextDay() }
    }

    fun prevDay() {
        val str: String = findViewById<EditText>(R.id.horDate).text.toString()
        if (DataValidator.checkIfValidDate(str)) {
            val tim: Long = SimpleDateFormat("dd/MM/yyyy").parse(str).time - 86400000
            val resstr: String = SimpleDateFormat("dd/MM/yyyy").format(Date(tim)).toString()
            findViewById<EditText>(R.id.horDate).setText(resstr)
            loadActivities(resstr)
        }
        else
            Toast.makeText(this, "wrong date!", Toast.LENGTH_SHORT).show()
    }

    fun nextDay() {
        val str: String = findViewById<EditText>(R.id.horDate).text.toString()
        if (DataValidator.checkIfValidDate(str)) {
            val tim: Long = SimpleDateFormat("dd/MM/yyyy").parse(str).time + 86400000
            val resstr: String = SimpleDateFormat("dd/MM/yyyy").format(Date(tim)).toString()
            findViewById<EditText>(R.id.horDate).setText(resstr)
            loadActivities(resstr)
        }
        else
            Toast.makeText(this, "wrong date!", Toast.LENGTH_SHORT).show()
    }

    fun select(mId: Int) {
        Log.d("tag1", mId.toString())
        Log.d("tag2", findViewById<TextView>(mId).text.toString())

        currSelectedView = mId

        val editBtn: ImageButton = findViewById(R.id.editButton)
        val deleteBtn: ImageButton = findViewById(R.id.deleteButton)
        editBtn.imageAlpha = 255
        deleteBtn.imageAlpha = 255
        editBtn.isEnabled = true
        deleteBtn.isEnabled = true

        val edCategory: EditText = findViewById(R.id.edCategory)
        val edStarted: EditText = findViewById(R.id.edStarted)
        val edFinished: EditText = findViewById(R.id.edFinished)
        val edNotes: EditText = findViewById(R.id.edNotes)


        val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy kk:mm")
        edCategory.setText(activities[mId]?.name ?: "NULL")
        edStarted.setText(dateFormat.format(activities[mId]!!.started).toString())
        edFinished.setText(dateFormat.format(activities[mId]!!.finished).toString())
        edNotes.setText(activities[mId]?.notes ?: "NULL")

        val insBtn: ImageButton = findViewById(R.id.enableAddButton)
        insBtn.setOnClickListener {
            enableInsert()
        }
        insBtn.setImageResource(R.drawable.add_icon)
    }

    fun loadActivities(day: String){
        if (!DataValidator.checkIfValidDate(day)) {
            Toast.makeText(this, "wrong date", Toast.LENGTH_SHORT).show()
            return
        }

        val editBtn: ImageButton = findViewById(R.id.editButton)
        val deleteBtn: ImageButton = findViewById(R.id.deleteButton)
        editBtn.imageAlpha = 75
        deleteBtn.imageAlpha = 75
        editBtn.isEnabled = false
        deleteBtn.isEnabled = false

        val edCategory: EditText = findViewById(R.id.edCategory)
        val edStarted: EditText = findViewById(R.id.edStarted)
        val edFinished: EditText = findViewById(R.id.edFinished)
        val edNotes: EditText = findViewById(R.id.edNotes)

        edCategory.isEnabled = false
        edStarted.isEnabled = false
        edFinished.isEnabled = false
        edNotes.isEnabled = false

        edCategory.text = null
        edStarted.text = null
        edFinished.text = null
        edNotes.text = null

       val insBtn: ImageButton = findViewById(R.id.enableAddButton)
        insBtn.setOnClickListener {
            enableInsert()
        }
        insBtn.setImageResource(R.drawable.add_icon)

        currSelectedView = -1

        val pixelRatio = 8;     // pixels per 1 minute
        val horLayout: RelativeLayout = findViewById(R.id.horLayout);
        horLayout.minimumWidth = 24*60*pixelRatio
        horLayout.setBackgroundResource(R.drawable.hbg)
        horLayout.removeAllViews()

        val fromTime = SimpleDateFormat("dd/MM/yyyy").parse(day).time
        handleGet(fromTime)

        actButtons.clear()
        var marg: Int
        var end: Int
        var layParam: FrameLayout.LayoutParams
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
            actButtons.last().setOnClickListener { select(index) }
            actButtons.last().setBackgroundResource(R.drawable.actborder)

            horLayout.addView(actButtons.last())
        }
    }

    fun handleGet(fromT: Long){
        val selectionArgs = arrayOf(fromT.toString(), (fromT + 86400000).toString(), fromT.toString(), fromT.toString())
        activities.clear()

        val db = this.dbHelper.writableDatabase
        val cs: Cursor = db.query(
            "Activities JOIN Categories ON Activities.name = Categories.id",
            null,
            "Activities.started > ? AND Activities.started < ? OR Activities.started < ? AND Activities.finished > ?",
            selectionArgs,
            null,
            null,
            "started ASC"
        )

        while (cs.moveToNext()) {
            activities[cs.getInt(0)] =
                Activity(cs.getInt(0), cs.getString(6), Date(cs.getLong(2)), Date(cs.getLong(3)), cs.getString(4))
        }
        cs.close()
        db.close()
    }

    fun handleInsert(name: Int, fromT: String, toT: String, notes: String){
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("name", name)
            put("started", SimpleDateFormat("dd.MM.yyyy kk:mm").parse(fromT).time)
            put("finished", SimpleDateFormat("dd.MM.yyyy kk:mm").parse(toT).time)
            put("notes", notes)
        }

        val newRowId = db.insert("Activities", null, values)

        db.close()
    }

    fun handleUpdate(id: Int, name: Int, fromT: String, toT: String, notes: String){
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("name", name)
            put("started", SimpleDateFormat("dd.MM.yyyy kk:mm").parse(fromT).time)
            put("finished", SimpleDateFormat("dd.MM.yyyy kk:mm").parse(toT).time)
            put("notes", notes)
        }

        val newRowId = db.update("Activities", values, "id = ?", arrayOf(id.toString()))

        db.close()
    }

    fun handleDelete(id: Int){
        val db = dbHelper.writableDatabase

        val deletedRows = db.delete("Activities", "id = ?", arrayOf(id.toString()))

        db.close()
    }


}