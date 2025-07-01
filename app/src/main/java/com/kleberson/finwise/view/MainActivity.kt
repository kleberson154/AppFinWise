package com.kleberson.finwise.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kleberson.finwise.R
import com.kleberson.finwise.controller.UserController
import com.kleberson.finwise.util.FormatBalance
import com.kleberson.finwise.util.VerticalSpaceItemDecoration

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val emailUser = intent.getStringExtra("email")
        if (emailUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val userController = UserController(this)
        val user = userController.getUserByEmail(this, emailUser)

        val userName = findViewById<TextView>(R.id.textViewNameUser)
        val userBalance = findViewById<TextView>(R.id.textViewBalanceUser)
        val formatBalance = FormatBalance()

        userName.text = user.name
        userBalance.text = formatBalance.format(user.balance)

        val btnAddSaldo = findViewById<Button>(R.id.buttonAddSaldo)
        val btnAddAtividade = findViewById<Button>(R.id.buttonAddAtividades)
        val btnFinalizar = findViewById<Button>(R.id.buttonFinalizar)

        val recycleActivities = findViewById<RecyclerView>(R.id.recycleAtividades)
        recycleActivities.addItemDecoration(VerticalSpaceItemDecoration(16))
        val atividades = userController.getActivitiesUser(this, emailUser)
        val adapter = ActivityAdapter(atividades)
        recycleActivities.layoutManager = LinearLayoutManager(this)
        recycleActivities.adapter = adapter

        if (user.balance <= 0){
            btnAddSaldo.text = "Adicionar Saldo"
        }else {
            btnAddSaldo.text = "Atualizar Saldo"
        }

        btnAddSaldo.setOnClickListener {
            startActivity(Intent(this, AddBalanceActivity::class.java).putExtra("email", emailUser))
        }

        btnAddAtividade.setOnClickListener {
            startActivity(Intent(this, AddActivitiesActivity::class.java).putExtra("email", emailUser))
        }

        btnFinalizar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val emailUser = intent.getStringExtra("email")
        if (emailUser != null) {
            val userController = UserController(this)
            val atividades = userController.getActivitiesUser(this, emailUser)
            val adapter = ActivityAdapter(atividades)
            val recycleActivities = findViewById<RecyclerView>(R.id.recycleAtividades)
            recycleActivities.adapter = adapter
        }
    }
}