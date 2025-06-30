package com.kleberson.finwise.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kleberson.finwise.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnAddSaldo = findViewById<Button>(R.id.buttonAddSaldo)
        val btnAddAtividade = findViewById<Button>(R.id.buttonAddAtividades)

        btnAddSaldo.setOnClickListener {
            startActivity(Intent(this, AddBalanceActivity::class.java))
        }

        btnAddAtividade.setOnClickListener {
            startActivity(Intent(this, AddActivitiesActivity::class.java))
        }
    }
}