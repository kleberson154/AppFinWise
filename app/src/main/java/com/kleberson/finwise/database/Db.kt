package com.kleberson.finwise.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import com.kleberson.finwise.model.Activity
import com.kleberson.finwise.model.User
import java.util.Date

class Db(context: Context): SQLiteOpenHelper(context, "finwise.db", null, 1) {
    override fun onCreate(db: android.database.sqlite.SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT, contact TEXT, balance REAL DEFAULT 0.0, image BLOB)")
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
        val values = ContentValues().apply {
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
            val idUser = cursor.getInt(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val email = cursor.getString(cursor.getColumnIndex("email"))
            val password = cursor.getString(cursor.getColumnIndex("password"))
            val contact = cursor.getString(cursor.getColumnIndex("contact"))
            val balance = cursor.getDouble(cursor.getColumnIndex("balance"))

            cursor.close()
            return User(idUser, name, email, password, contact, balance)
        } else{
            cursor.close()
            return null
        }
    }

    fun insertBalance(emailUser: String, value: Double): Boolean {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT id FROM users WHERE email = ?", arrayOf(emailUser))

        if(cursor.moveToFirst()){
            val idIndex = cursor.getColumnIndex("id")
            if (idIndex != -1) {
                val userId = cursor.getInt(idIndex)
                val values = ContentValues().apply {
                    put("balance", value)
                }
                cursor.close()
                db.update("users", values, "id = ?", arrayOf(userId.toString()))
                db.close()
                return true
            }else{
                return false
            }
        } else {
            cursor.close()
            db.close()
            return false
        }
    }

    fun insertActivity(user: User, activityName: String, activityType: String, activitySpentOrReceived: String, activityPrice: Double) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", activityName)
            put("category", activityType)
            put("type", activitySpentOrReceived)
            put("price", activityPrice)
            put("date", Date().toString())
            put("user_id", user.id)
        }
        db.insert("activities", null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getActivitiesByUser(id: Int): MutableList<Activity> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM activities WHERE user_id = ?", arrayOf(id.toString()))
        val activities = mutableListOf<Activity>()

        if (cursor.moveToFirst()) {
            do {
                val idActivity = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val category = cursor.getString(cursor.getColumnIndex("category"))
                val type = cursor.getString(cursor.getColumnIndex("type"))
                val price = cursor.getDouble(cursor.getColumnIndex("price"))
                val date = Date(cursor.getString(cursor.getColumnIndex("date")))

                activities.add(Activity(idActivity, name, category, type, price, date))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return activities
    }

    fun deleteActivity(activity: Activity) {
        val db = writableDatabase
        db.delete("activities", "id = ?", arrayOf(activity.id.toString()))
        db.close()
    }

    fun saveUserImage(emailUser: String?, imageBytes: ByteArray) {
        val db = writableDatabase
        db.execSQL("UPDATE users SET image = ? WHERE email = ?", arrayOf(imageBytes, emailUser))
        db.close()
    }

    @SuppressLint("Range")
    fun getUserImage(emailUser: String): ByteArray? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT image FROM users WHERE email = ?", arrayOf(emailUser))

        return if (cursor.moveToFirst()) {
            val imageBytes = cursor.getBlob(cursor.getColumnIndex("image"))
            cursor.close()
            db.close()
            imageBytes
        } else {
            cursor.close()
            db.close()
            null
        }
    }
}