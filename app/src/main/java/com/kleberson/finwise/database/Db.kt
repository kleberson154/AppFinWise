package com.kleberson.finwise.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import com.kleberson.finwise.model.User

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

    fun checkUserExists(email: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun insertUser(user: User) {
        val db = writableDatabase
        val values = android.content.ContentValues().apply {
            put("name", user.name)
            put("email", user.email)
            put("password", user.password)
            put("contact", user.contact)
        }
        db.insert("users", null, values)
        db.close()
    }

    fun verifyPassword(email: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", arrayOf(email, password))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    @SuppressLint("Range")
    fun getUserByEmail(emailUser: String): User? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", arrayOf(emailUser))

        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val email = cursor.getString(cursor.getColumnIndex("email"))
            val password = cursor.getString(cursor.getColumnIndex("password"))
            val contact = cursor.getString(cursor.getColumnIndex("contact"))
            val balance = cursor.getDouble(cursor.getColumnIndex("balance"))

            cursor.close()
            return User(name, email, password, contact, balance)
        } else{
            cursor.close()
            return null
        }
    }
}