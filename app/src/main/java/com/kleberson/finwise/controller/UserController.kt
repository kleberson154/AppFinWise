package com.kleberson.finwise.controller

import android.content.Context
import android.widget.Toast
import com.kleberson.finwise.database.Db
import com.kleberson.finwise.exception.PasswordNotEqualsException
import com.kleberson.finwise.exception.UserExistException
import com.kleberson.finwise.exception.UserNotExistException
import com.kleberson.finwise.model.User

class UserController(context: Context) {
    private val db = Db(context)

    fun registerUser(context: Context, name: String, email: String, contact: String, password: String, confirmPassword: String): Boolean {
        val userExists = db.checkUserExists(email)

        try {
            if (userExists){
                throw UserExistException()
            }

            if (password != confirmPassword) {
                throw PasswordNotEqualsException()
            }

            val user = User(name = name, email = email, contact = contact, password = password)

            db.insertUser(user)
            return true
        }catch (e: UserExistException) {
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            return false
        }catch (e: PasswordNotEqualsException) {
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            return false
        }
    }

    fun loginUser(context: Context, email: String, password: String): Boolean {
        val db = Db(context)
        val userExists = db.checkUserExists(email)
        val passwordMatches = db.verifyPassword(email ,password)

        try {
            if (!userExists) {
                throw UserNotExistException("Usuário não encontrado")
            }

            if (!passwordMatches) {
                throw PasswordNotEqualsException("Senha incorreta")
            }
            return true
        } catch (e: UserNotExistException) {
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            return false
        } catch (e: PasswordNotEqualsException) {
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            return false
        }
    }

    fun getUserByEmail(context: Context,emailUser: String): User {
        val db = Db(context)
        val user = db.getUserByEmail(emailUser) ?: throw UserNotExistException("Usuário não encontrado")

        return user
    }

    fun addBalance(context: Context, emailUser: String, value: Double) {
        val db = Db(context)

        try {
            val user = db.insertBalance(emailUser, value)
            if (!user) {
                throw UserNotExistException("Usuário não encontrado")
            }
        }catch (e: UserNotExistException) {
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }
}