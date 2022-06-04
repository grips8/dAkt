package com.example.dakt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StatisticsView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_view)

        findViewById<Button>(R.id.switchBackFromStatistics).setOnClickListener {
            finish()
        }
    }
}