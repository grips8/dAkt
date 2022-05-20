package com.example.dakt

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dakt.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var dbHelper: DatabaseHelper = DatabaseHelper(this)
    private val activities: MutableList<Activity> = mutableListOf()

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

        findViewById<Button>(R.id.testButton).setOnClickListener {
            handleGet("10.12.2020 15:24", "11.12.2020 16:00")
            handleInsert(1, "11.12.2020 15:24", "11.12.2020 16:00", "lorem ipsum")
        }
    }

    fun handleGet(fromT: String, toT: String){
        val fromTime = SimpleDateFormat("dd.MM.yyyy kk:mm").parse(fromT).time
        val toTime = SimpleDateFormat("dd.MM.yyyy kk:mm").parse(toT).time
        val selectionArgs = arrayOf(fromTime.toString(), toTime.toString())

        val db = this.dbHelper.writableDatabase
        val cs: Cursor = db.query(
            "Activities JOIN Categories ON Activities.name = Categories.id",
            null,
            "Activities.started > ? AND Activities.finished < ?",
            selectionArgs,
            null,
            null,
            null
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
}