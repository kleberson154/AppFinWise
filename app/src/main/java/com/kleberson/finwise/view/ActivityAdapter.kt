package com.kleberson.finwise.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kleberson.finwise.R
import com.kleberson.finwise.model.Activity
import com.kleberson.finwise.util.FormatBalance
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

class ActivityAdapter(private val atividades: List<Activity>): RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome = itemView.findViewById<TextView>(R.id.textViewNameActivity)
        val category = itemView.findViewById<TextView>(R.id.textViewTypeActivity)
        val data = itemView.findViewById<TextView>(R.id.textViewDate)
        val valor = itemView.findViewById<TextView>(R.id.textViewPriceActivity)
        val signal = itemView.findViewById<TextView>(R.id.textViewPosNeg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_atividades, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val atividade = atividades[position]
        holder.nome.text = atividade.name
        holder.category.text = atividade.category
        holder.data.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(atividade.date)
        holder.valor.text = FormatBalance().format(atividade.price)
        if(atividade.type == "Gasto"){
            holder.signal.text = "-$"
            holder.signal.setTextColor(holder.itemView.context.getColor(R.color.negative))
            holder.valor.setTextColor(holder.itemView.context.getColor(R.color.negative))
        } else {
            holder.signal.text = "+$"
            holder.signal.setTextColor(holder.itemView.context.getColor(R.color.primary))
            holder.valor.setTextColor(holder.itemView.context.getColor(R.color.primary))
        }
    }

    override fun getItemCount() = atividades.size
}