package com.example.dakt

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import java.text.SimpleDateFormat
import java.util.*

class MVCModel (context: Context){
    private val dbHelper: DatabaseHelper = DatabaseHelper(context)
    private val activities: MutableMap<Int, Activity> = mutableMapOf()
    private val categories: MutableMap<Int, Category> = mutableMapOf()
    private var currSelectedView: Int = -1
    private var currDate: Long = 0
    private val activitiesStat: MutableList<Activity> = mutableListOf()

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
                Activity(cs.getInt(0), cs.getString(6), Date(cs.getLong(2)), Date(cs.getLong(3)), cs.getString(4), cs.getLong(2), cs.getLong(3))
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

    fun handleCategories(){
        categories.clear()

        val db = this.dbHelper.readableDatabase
        val cs: Cursor = db.query(
            "Categories",
            null,
            null,
            null,
            null,
            null,
            null
        )

        while (cs.moveToNext()) {
            categories[cs.getInt(0)] =
                Category(cs.getInt(0), cs.getString(1), cs.getString(2), (cs.getInt(3) == 1))
        }
        cs.close()
        db.close()
    }

    fun getCategories() : MutableMap<Int, Category> {
        return categories
    }

    // error when such index doesn't exist, but I'm pretty sure it does in all calls
    fun getCategory(mId: Int) : Category {
        return categories[mId]!!
    }

    fun handleInsertCategory(name: String, description: String, starred: Boolean) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("description", description)
            put("starred", starred)
        }

        val newRowId = db.insert("Categories", null, values)

        db.close()
    }

    fun handleActivitiesStatistics(fromT: Long, toT: Long) {
        activitiesStat.clear()
        val db = this.dbHelper.readableDatabase
        val cs: Cursor = db.query(
            "Activities",
            null,
            null,
            null,
            null,
            null,
            "started ASC"
        )

        while (cs.moveToNext()) {
            activitiesStat.add(Activity(cs.getInt(0), "Placeholder", Date(cs.getLong(2)), Date(cs.getLong(3)), cs.getString(4), cs.getLong(2), cs.getLong(3)))
        }
        cs.close()
        db.close()
    }

    // if first element in activities is before fromT, the function doesn't work properly
    fun daysOfWeek(fromT: Long, toT: Long) : IntArray {      // returns activities per day of week (0 -> monday,..., 6->sunday)
        var res: IntArray = intArrayOf(0, 0, 0, 0, 0, 0, 0)
        var index: Int = ((((fromT + 7200000) / 86400000) + 3) % 7).toInt()     // calculating day of week (+7200000 is UMT to GMT timezone)
        // prob should replace with some getTimezone(), but it's good for now
        var actIndex: Int = 0
        var currentDay: Long = fromT
        while (currentDay < toT + 86400000) {
            while (actIndex < activitiesStat.count() &&
                activitiesStat[actIndex].startedLong >= currentDay &&
                activitiesStat[actIndex].startedLong < currentDay + 86400000) {
                res[index%7]++
                actIndex++
            }
            currentDay += 86400000
            index++
        }

        return res
    }
}