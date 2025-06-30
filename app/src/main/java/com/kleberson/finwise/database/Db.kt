package com.kleberson.finwise.database

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper

class Db(context: Context): SQLiteOpenHelper(context, "finwise.db", null, 1) {
    override fun onCreate(db: android.database.sqlite.SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT, contact TEXT, balance REAL DEFAULT 0.0)")
        db.execSQL("CREATE TABLE IF NOT EXISTS activities (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, category TEXT, type TEXT, price REAL, date TEXT, user_id INTEGER, FOREIGN KEY(user_id) REFERENCES users(id))")
    }

    override fun onUpgrade(db: android.database.sqlite.SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS saldo")
        db.execSQL("DROP TABLE IF EXISTS atividades")
        onCreate(db)
    }

}