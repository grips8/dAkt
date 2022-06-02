package com.example.dakt

import android.content.ContentValues
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

        findViewById<Button>(R.id.dateButton).setOnClickListener {
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

    fun select(view: View) {
        Log.d("tag1", view.id.toString())
        Log.d("tag2", findViewById<TextView>(view.id).text.toString())

        val edCategory: EditText = findViewById(R.id.edCategory)
        val edStarted: EditText = findViewById(R.id.edStarted)
        val edFinished: EditText = findViewById(R.id.edFinished)
        val edNotes: EditText = findViewById(R.id.edNotes)

        edCategory.inputType = InputType.TYPE_NULL
        edStarted.inputType = InputType.TYPE_NULL
        edFinished.inputType = InputType.TYPE_NULL
        edNotes.inputType = InputType.TYPE_NULL


        edCategory.setText(activities[view.id]?.name ?: "NULL")
        edStarted.setText(activities[view.id]?.started.toString() ?: "NULL")
        edFinished.setText(activities[view.id]?.finished.toString() ?: "NULL")
        edNotes.setText(activities[view.id]?.notes ?: "NULL")

    }

    fun loadActivities(day: String){
        if (!DataValidator.checkIfValidDate(day)) {
            Toast.makeText(this, "wrong date", Toast.LENGTH_SHORT).show()
            return
        }
        val pixelRatio = 8;
        val horLayout: RelativeLayout = findViewById(R.id.horLayout);
        horLayout.minimumWidth = 24*60*pixelRatio
        horLayout.setBackgroundColor(Color.LTGRAY)
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
            if (value.finished.time > fromTime + 86400)
                end = 86400
            else
                end = (value.finished.time - fromTime).toInt()
            if (value.started.time <= fromTime)
                marg = 0
            else
                marg = (value.started.time - fromTime).toInt()
            layParam = FrameLayout.LayoutParams((end - marg) / pixelRatio, FrameLayout.LayoutParams.MATCH_PARENT)
            layParam.setMargins(marg / pixelRatio, 0, 0, 0)
            actButtons.last().layoutParams = layParam
            actButtons.last().text = value.name
            actButtons.last().setBackgroundColor(Color.DKGRAY)
            actButtons.last().isClickable = true
            actButtons.last().setOnClickListener { select(actButtons.last())}

            horLayout.addView(actButtons.last())
        }
    }

    fun handleGet(fromT: Long){
        val selectionArgs = arrayOf(fromT.toString(), (fromT + 86400).toString(), fromT.toString(), (fromT + 86400).toString())
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