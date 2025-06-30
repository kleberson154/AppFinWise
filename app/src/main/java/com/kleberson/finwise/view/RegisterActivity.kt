package com.kleberson.finwise.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.kleberson.finwise.R
import com.kleberson.finwise.controller.UserController

class RegisterActivity: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val fullName = findViewById<EditText>(R.id.editTextNameRegister)
        val email = findViewById<EditText>(R.id.editTextEmailRegister)
        val numberTel = findViewById<EditText>(R.id.editTextNumberRegister)
        val password = findViewById<EditText>(R.id.editTextPasswordRegister)
        val confirmPassword = findViewById<EditText>(R.id.editTextPasswordConfirmRegister)
        val btnRegister = findViewById<Button>(R.id.buttonSignUp)
        val linkLogin = findViewById<TextView>(R.id.textViewLinkLogin)


        btnRegister.setOnClickListener {
            if (fullName.text.isEmpty() || email.text.isEmpty() || numberTel.text.isEmpty() ||
                password.text.isEmpty() || confirmPassword.text.isEmpty()) {
                fullName.error = "Preencha todos os campos"
                email.error = "Preencha todos os campos"
                numberTel.error = "Preencha todos os campos"
                password.error = "Preencha todos os campos"
                confirmPassword.error = "Preencha todos os campos"
            } else {
                val userController = UserController(this)
                val isRegistered = userController.registerUser(
                    this,
                    fullName.text.toString(),
                    email.text.toString(),
                    numberTel.text.toString(),
                    password.text.toString(),
                    confirmPassword.text.toString()
                )
                if (isRegistered) {
                    val sharedPref = getSharedPreferences("finwise_prefs", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("user_email", email.text.toString())
                        putString("user_name", fullName.text.toString())
                        apply()
                    }
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }

        linkLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}