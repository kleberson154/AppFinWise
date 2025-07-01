package com.kleberson.finwise.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.kleberson.finwise.R
import com.kleberson.finwise.controller.UserController

class AddActivitiesActivity: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_activities)
        enableEdgeToEdge()
        val userEmail = intent.getStringExtra("email")
        if (userEmail == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val userController = UserController(this)

        val optionsTypeActivity = arrayOf("Alimentação", "Transporte", "Saúde", "Educação", "Lazer", "Casa", "Roupas", "Outros")
        val optionsSpentOrReceived = arrayOf("Gasto", "Recebido")

        val nameActivity = findViewById<EditText>(R.id.editTextNameActivity)
        val typeActivity = findViewById<Spinner>(R.id.spinnerTypeActivity)
        val spentOrReceived = findViewById<Spinner>(R.id.spinnerSpentOrReceived)
        val price = findViewById<EditText>(R.id.editTextInputPrice)

        val adapterType = ArrayAdapter(this, android.R.layout.simple_spinner_item, optionsTypeActivity)
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeActivity.adapter = adapterType

        val adapterSpentOrReceived = ArrayAdapter(this, android.R.layout.simple_spinner_item, optionsSpentOrReceived)
        adapterSpentOrReceived.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spentOrReceived.adapter = adapterSpentOrReceived

        val btnAddActivity = findViewById<Button>(R.id.buttonAddActivity)

        btnAddActivity.setOnClickListener{
            if (nameActivity.text.isEmpty() || price.text.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val activityName = nameActivity.text.toString()
            val activityType = typeActivity.selectedItem.toString()
            val activitySpentOrReceived = spentOrReceived.selectedItem.toString()
            val activityPrice = price.text.toString().toDouble()

            userController.addActivity(
                this,
                userEmail,
                activityName,
                activityType,
                activitySpentOrReceived,
                activityPrice
            )

            Toast.makeText(this, "Atividade adicionada com sucesso!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java).putExtra("email", userEmail))
            finish()
        }
    }
}