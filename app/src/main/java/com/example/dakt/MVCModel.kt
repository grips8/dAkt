package com.example.dakt

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import java.text.SimpleDateFormat
import java.util.*

class MVCModel (context: Context){
    private val dbHelper: DatabaseHelper = DatabaseHelper(context)
    private val activities: MutableMap<Int, Activity> = mutableMapOf()
    private var currSelectedView: Int = -1
    private var currDate: Long = 0

    fun getCurrDate() : Long { return currDate }

    fun changeSelectedView(index: Int) { currSelectedView = index }

    fun getActivities() : MutableMap<Int, Activity> { return activities }

    // error when such index doesn't exist, but I'm pretty sure it does in all calls
    fun getActivity(mId: Int) : Activity { return activities[mId]!! }

    fun handleGet(fromT: Long){
        val selectionArgs = arrayOf(fromT.toString(), (fromT + 86400000).toString(), fromT.toString(), fromT.toString())
        currDate = fromT
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

    fun fetchCategory(str: String) : Int {
        val db = dbHelper.readableDatabase
        var res: Int = -1

        val selectionArgs = arrayOf(str)
        val cols = arrayOf("Id")
        val cs: Cursor = db.query(
            "Categories",
            cols,
            "name = ?",
            selectionArgs,
            null,
            null,
            null
        )

        if (cs.moveToNext())
            res = cs.getInt(0)
        cs.close()
        db.close()

        return res
    }

    fun handleInsert(categoryId: Int, started: String, finished: String, notes: String) {
        val date: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy kk:mm")
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", categoryId)
            put("started", date.parse(started).time)
            put("finished", date.parse(finished).time)
            put("notes", notes)
        }

        val newRowId = db.insert("Activities", null, values)

        db.close()
    }

    fun handleUpdate(categoryId: Int, started: String, finished: String, notes: String){
        val date: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy kk:mm")
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("name", categoryId)
            put("started", date.parse(started).time)
            put("finished", date.parse(finished).time)
            put("notes", notes)
        }

        val newRowId = db.update("Activities", values, "id = ?", arrayOf(currSelectedView.toString()))

        db.close()
    }

    fun handleDelete(){
        if (currSelectedView != -1) {       // almost certainly won't be -1, but why not check
            val db = dbHelper.writableDatabase

            val deletedRows = db.delete("Activities", "id = ?", arrayOf(currSelectedView.toString()))

            db.close()
        }
    }
}