package com.example.dakt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class CategoriesView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_view)

        findViewById<Button>(R.id.switchBackFromCategories).setOnClickListener {
            finish()
        }
    }

}