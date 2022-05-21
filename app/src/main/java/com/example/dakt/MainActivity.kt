package com.example.dakt

import android.app.DatePickerDialog
import android.content.ContentValues
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dakt.databinding.ActivityMainBinding
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var dbHelper: DatabaseHelper = DatabaseHelper(this)
    private val activities: MutableList<Activity> = mutableListOf()
    private val actButtons: MutableList<Button> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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
    }

    fun select(view: View) {
        Log.d("tag1", view.id.toString())
        Log.d("tag2", findViewById<TextView>(view.id).text.toString())
    }

    fun loadActivities(day: String){
        val pixelRatio = 8;
        val horLayout: RelativeLayout = findViewById(R.id.horLayout);
        horLayout.minimumWidth = 24*60*pixelRatio
        horLayout.setBackgroundColor(Color.LTGRAY)

        val fromTime = SimpleDateFormat("dd/MM/yyyy").parse(day).time
        Log.d("myTag", fromTime.toString())
        handleGet(fromTime)


        actButtons.clear()
        var marg: Int
        var end: Int
        var layParam: FrameLayout.LayoutParams
        activities.forEach{
            actButtons.add(Button(this))
            actButtons.last().id = it.id
            if (it.finished.time > fromTime + 86400)
                end = 86400
            else
                end = (it.finished.time - fromTime).toInt()
            if (it.started.time <= fromTime)
                marg = 0
            else
                marg = (it.started.time - fromTime).toInt()
            layParam = FrameLayout.LayoutParams((end - marg) / pixelRatio, FrameLayout.LayoutParams.MATCH_PARENT)
            layParam.setMargins(marg / pixelRatio, 0, 0, 0)
            actButtons.last().layoutParams = layParam
            actButtons.last().text = it.name
            actButtons.last().setBackgroundColor(Color.DKGRAY)
//            actButtons.last().isClickable = true
//            actButtons.last().setOnclickListener()

            horLayout.addView(actButtons.last())
        }
//        val myButton: Button = Button(this)
//        myButton.width = 1000
//        val myButton2: Button = Button(this)
//        myButton.width = 1000
//        myButton2.width = 1000
//
//        horLayout.addView(myButton)
//        horLayout.addView(myButton2)
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
            activities.add(Activity(cs.getInt(0), cs.getString(6), Date(cs.getLong(2)), Date(cs.getLong(3)), cs.getString(4)))
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