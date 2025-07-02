package com.kleberson.finwise.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kleberson.finwise.R
import com.kleberson.finwise.controller.UserController
import com.kleberson.finwise.util.FormatBalance
import com.kleberson.finwise.util.VerticalSpaceItemDecoration
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageUri = data?.data
            val imageBytes = uriToByteArray(this, imageUri!!)
            val userController = UserController(this)
            val emailUser = intent.getStringExtra("email")

            userController.saveUserImage(emailUser, imageBytes)
            imageView.setImageBitmap(byteArrayToBitmap(imageBytes))
        }
    }

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
        val user = userController.getUserByEmail(emailUser)
        val atividades = userController.getActivitiesUser(emailUser)

        imageView = findViewById(R.id.imageViewPerfil)
        val imageBytes = userController.getUserImage(emailUser)
        if (imageBytes != null && imageBytes.isNotEmpty()) {
            imageView.setImageBitmap(byteArrayToBitmap(imageBytes))
        }

        imageView.setOnClickListener {
            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"
            pickImage.launch(pickIntent)
        }

        val userName = findViewById<TextView>(R.id.textViewNameUser)
        userName.text = user.name
        atualizarSaldo()

        val btnAddSaldo = findViewById<Button>(R.id.buttonAddSaldo)
        val btnAddAtividade = findViewById<Button>(R.id.buttonAddAtividades)
        val btnFinalizar = findViewById<Button>(R.id.buttonFinalizar)

        val recycleActivities = findViewById<RecyclerView>(R.id.recycleAtividades)
        recycleActivities.addItemDecoration(VerticalSpaceItemDecoration(16))
        val adapter = ActivityAdapter(atividades) { atualizarSaldo() }
        recycleActivities.layoutManager = LinearLayoutManager(this)
        recycleActivities.adapter = adapter

        if (user.balance == 0.0){
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
            val atividades = userController.getActivitiesUser(emailUser)
            val adapter = ActivityAdapter(atividades) { atualizarSaldo() }
            val recycleActivities = findViewById<RecyclerView>(R.id.recycleAtividades)
            recycleActivities.adapter = adapter
        }
    }

    fun atualizarSaldo() {
        val emailUser = intent.getStringExtra("email") ?: return
        val userController = UserController(this)
        val user = userController.getUserByEmail(emailUser)
        val atividades = userController.getActivitiesUser(emailUser)
        val userBalance = findViewById<TextView>(R.id.textViewBalanceUser)
        val signalSaldo = findViewById<TextView>(R.id.textViewSignalSaldo)
        val formatBalance = FormatBalance()
        var valueAtividades = 0.0
        for (atividade in atividades){
            if (atividade.type == "Gasto") {
                valueAtividades -= atividade.price
            } else {
                valueAtividades += atividade.price
            }
        }
        val total = user.balance + valueAtividades
        if (total < 0){
            signalSaldo.text = "-$"
            userBalance.text = formatBalance.format(abs(total))
        }else{
            signalSaldo.text = "$"
            userBalance.text = formatBalance.format(total)
        }


        val imageGanhoPerda = findViewById<ImageView>(R.id.imageViewGanhoPerda)
        val textGanhoPerda = findViewById<TextView>(R.id.textViewGanhoPerda)
        val signalGanhoPerda = findViewById<TextView>(R.id.textViewSignalGanhoPerda)
        val valueGanhoPerda = findViewById<TextView>(R.id.textViewGanhoPerdaValue)

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
    }

    fun uriToByteArray(context: Context, uri: Uri): ByteArray {
        return context.contentResolver.openInputStream(uri)?.readBytes() ?: ByteArray(0)
    }

    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}