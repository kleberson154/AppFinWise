package com.kleberson.finwise.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.kleberson.finwise.R
import com.kleberson.finwise.controller.UserController

class AddBalanceActivity: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_balance)
        val emailUser = intent.getStringExtra("email")

        val btnBalance = findViewById<Button>(R.id.buttonAddActivity)
        val inputBalance = findViewById<EditText>(R.id.editTextInputBalance)

        val userController = UserController(this)

        btnBalance.setOnClickListener{
            if (inputBalance.text.isEmpty()) {
                Toast.makeText(this, "Por favor, insira um valor válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (emailUser != null) {
                userController.addBalance(this, emailUser, inputBalance.text.toString().toDouble())
            }else{
                Toast.makeText(this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }

            Toast.makeText(this, "Saldo adicionado com sucesso!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java).putExtra("email", emailUser))
            finish()
        }
    }
}