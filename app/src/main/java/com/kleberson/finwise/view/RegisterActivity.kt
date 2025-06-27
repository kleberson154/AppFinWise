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
            // Handle registration logic here
            // For example, validate inputs and send data to the server
        }

        linkLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}