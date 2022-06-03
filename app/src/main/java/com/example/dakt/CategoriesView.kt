package com.example.dakt

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

class CategoriesView : AppCompatActivity() {
    private val model: MVCModel = MVCModel(this)
    private lateinit var catScrollView: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_view)

        catScrollView = findViewById(R.id.catScrollView)

        findViewById<Button>(R.id.switchBackFromCategories).setOnClickListener {

        }
    }

// Start of >>>>VIEV<<<< functions
    fun generateCategories (categories: MutableMap<Int, Category>) {
        catScrollView.removeAllViews()

        var layParam: FrameLayout.LayoutParams
        val textViews: MutableList<TextView> = mutableListOf()
        for ((key, value) in categories) {
            textViews.add(TextView(this))
            textViews.last().id = value.id
            layParam = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 50) // ????? xD
            layParam.setMargins(0, 0, 0, 0)
            textViews.last().layoutParams = layParam
            textViews.last().text = value.name
            textViews.last().setBackgroundColor(Color.DKGRAY)
            textViews.last().isClickable = true
            val index = textViews.last().id
            textViews.last().setOnClickListener { catSelectClick(index) }
            textViews.last().setBackgroundResource(R.drawable.actborder)
            catScrollView.addView(textViews.last())
        }
    }
// End of >>>>VIEV<<<< functions

// Start of >>>>CONTROLLER<<<< functions
    private fun catLoadCategoriesClick() {
//        model.handleCategories()
    }
    private fun catSelectClick(mId: Int) {

    }
// End of >>>>CONTROLLER<<<< functions


}