package com.takahiroApp.simplememo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION){
    companion object{
        private const val DATABASE_NAME="simplememo.db"
        private const val DATABASE_VERSION=1
    }

    override fun onCreate(db: SQLiteDatabase) {
        val sb=StringBuilder()
        sb.append("CREATE TABLE simplememo (")
        sb.append("_id INTEGER PRIMARY KEY,")
        sb.append("title TEXT,")
        sb.append("text TEXT,")
        sb.append("date TEXT")
        sb.append(");")
        val sql=sb.toString()
        db.execSQL(sql)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}

}