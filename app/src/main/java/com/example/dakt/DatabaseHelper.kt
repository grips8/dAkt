package com.example.dakt
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "ActDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE Categories (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT)"
        )
        db.execSQL("CREATE TABLE Activities (" +
                "id INTEGER PRIMARY KEY," +
                "name INTEGER," +
                "started INTEGER," +
                "finished INTEGER," +
                "notes TEXT)"
        )
        db.execSQL("CREATE TABLE BRActivities (" +
                "id INTEGER PRIMARY KEY," +
                "name INTEGER," +
                "started INTEGER," +
                "finished INTEGER," +
                "notes TEXT," +
                "pages INTEGER," +
                "title TEXT," +
                "genre TEXT)"
        )
        db.execSQL("CREATE TABLE BActivities (" +
                "id INTEGER PRIMARY KEY," +
                "name INTEGER," +
                "started INTEGER," +
                "finished INTEGER," +
                "notes TEXT," +
                "calories INTEGER," +
                "cuisine TEXT," +
                "meal TEXT)"
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
        // do nothing lol?
    }


}