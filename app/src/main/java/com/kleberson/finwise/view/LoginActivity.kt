package com.kleberson.finwise.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.kleberson.finwise.R

class LoginActivity: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val inputEmail = findViewById<EditText>(R.id.editTextEmailRegister)
        val inputPassword = findViewById<EditText>(R.id.editTextPasswordRegister)
        val btnLogin = findViewById<Button>(R.id.buttonLogin)
        val btnSendSignUp = findViewById<Button>(R.id.buttonSignUp)

        btnLogin.setOnClickListener {
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()

            startActivity(Intent(this, MainActivity::class.java).apply {
                putExtra("email", email)
                putExtra("password", password)
            })
        }

        btnSendSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}