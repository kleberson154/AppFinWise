package com.kleberson.finwise.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
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

        val imageGanhoPerda = findViewById<ImageView>(R.id.imageViewGanhoPerda)
        val textGanhoPerda = findViewById<TextView>(R.id.textViewGanhoPerda)
        val signalGanhoPerda = findViewById<TextView>(R.id.textViewSignalGanhoPerda)
        val valueGanhoPerda = findViewById<TextView>(R.id.textViewGanhoPerdaValue)

        var valueAtividades = 0.0

        for (atividade in atividades){
            if (atividade.type == "Gasto") {
                valueAtividades -= atividade.price
            } else {
                valueAtividades += atividade.price
            }
        }

        if (valueAtividades >= 0){
            imageGanhoPerda.setImageResource(R.drawable.arrowup)
            textGanhoPerda.text = "Lucros"
            signalGanhoPerda.text = "+$"
            signalGanhoPerda.setTextColor(resources.getColor(R.color.text_color))
            valueGanhoPerda.text = formatBalance.format(valueAtividades)
            valueGanhoPerda.setTextColor(resources.getColor(R.color.text_color))
        } else if (valueAtividades < 0) {
            imageGanhoPerda.setImageResource(R.drawable.arrowdown)
            textGanhoPerda.text = "Perdas"
            signalGanhoPerda.text = "-$"
            signalGanhoPerda.setTextColor(resources.getColor(R.color.negative))
            valueGanhoPerda.text = formatBalance.format(valueAtividades * -1)
            valueGanhoPerda.setTextColor(resources.getColor(R.color.negative))
        }

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