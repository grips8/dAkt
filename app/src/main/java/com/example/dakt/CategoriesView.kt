package com.example.dakt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*

class CategoriesView : AppCompatActivity() {
    private val model: MVCModel = MVCModel(this)
    private lateinit var catScrollView: LinearLayout
    private lateinit var catEdName: EditText
    private lateinit var catEdDesc: EditText
    private lateinit var catCheckbox: CheckBox
    private lateinit var catAddButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_view)

        catScrollView = findViewById(R.id.catScrollView)
        catEdName = findViewById(R.id.catEdName)
        catEdDesc = findViewById(R.id.catEdDescription)
        catCheckbox = findViewById(R.id.catStarredCheckbox)
        catAddButton = findViewById(R.id.catAddButton)

        catLoadCategoriesClick()

        findViewById<Button>(R.id.switchBackFromCategories).setOnClickListener {
            finish()
        }

        findViewById<ImageButton>(R.id.catAddButton).setOnClickListener {
            catEnableAddClick()
        }
    }

// Start of >>>>VIEW<<<< functions
    private fun catClearEdits() {
        catEdName.text = null
        catEdDesc.text = null
        catCheckbox.isChecked = false
    }
    private fun catSwitchEdits(enabled: Boolean) {
        catEdName.isEnabled = enabled
        catEdDesc.isEnabled = enabled
        catCheckbox.isEnabled = enabled
    }
    private fun catSwitchAddButton(mode: Boolean) {     // true -> confirmButton; false -> enableButton
        if (mode) {
            catAddButton.setOnClickListener { catConfirmInsertClick() }
            catAddButton.setImageResource(R.drawable.tick_icon)
        }
        else {
            catAddButton.setOnClickListener { catEnableAddClick() }
            catAddButton.setImageResource(R.drawable.add_icon)
        }
    }
    private fun catMakeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun catEnabledAdd() {
        catClearEdits()
        catSwitchEdits(true)
        catSwitchAddButton(true)
    }
    private fun catSelected(mId: Int) {
        catSwitchAddButton(false)
        catSwitchEdits(false)

        val cat: Category = model.getCategory(mId)

        catEdName.setText(cat.name)
        catEdDesc.setText(cat.description)
        catCheckbox.isChecked = cat.starred
    }
    private fun generateCategories () {
        val categories: MutableMap<Int, Category> = model.getCategories()

        catClearEdits()
        catScrollView.removeAllViews()

        var layParam: FrameLayout.LayoutParams
        val textViews: MutableList<TextView> = mutableListOf()
        for ((key, value) in categories) {
            textViews.add(TextView(this))
            textViews.last().id = value.id
            layParam = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 125)
            layParam.setMargins(10, 5, 10, 0)
            textViews.last().layoutParams = layParam
            textViews.last().text = value.name
            textViews.last().setPadding(15, 0, 50, 0)
            textViews.last().textAlignment = View.TEXT_ALIGNMENT_GRAVITY
            textViews.last().gravity = Gravity.CENTER_VERTICAL
            textViews.last().isClickable = true
            textViews.last().textSize = 20f
            if (value.starred)
                textViews.last().setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.star, 0)
            else
                textViews.last().setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.star_outline, 0)
            val index = textViews.last().id
            textViews.last().setOnClickListener { catSelectClick(index) }
            textViews.last().setBackgroundResource(R.drawable.catborder)
            catScrollView.addView(textViews.last())
        }
    }
// End of >>>>VIEW<<<< functions

// Start of >>>>CONTROLLER<<<< functions
    private fun catLoadCategoriesClick() {
        model.handleCategories()
        generateCategories()
    }
    private fun catSelectClick(mId: Int) {
        catSelected(mId)
    }
    private fun catConfirmInsertClick() {
        if (model.fetchCategory(catEdName.text.toString()) == -1) {
            model.handleInsertCategory(catEdName.text.toString(), catEdDesc.text.toString(), catCheckbox.isChecked)
            catLoadCategoriesClick()
        }
        else
            catMakeToast("Name already taken!")
    }
    private fun catEnableAddClick() {
        catEnabledAdd()
    }
// End of >>>>CONTROLLER<<<< functions


}