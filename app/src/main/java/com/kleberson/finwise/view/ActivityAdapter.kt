package com.kleberson.finwise.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kleberson.finwise.R
import com.kleberson.finwise.controller.UserController
import com.kleberson.finwise.model.Activity
import com.kleberson.finwise.util.FormatBalance
import java.text.SimpleDateFormat
import java.util.Locale

class ActivityAdapter(private val atividades: MutableList<Activity>, private val onItemRemoved: () -> Unit): RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.textViewNameActivity)
        val category: TextView = itemView.findViewById(R.id.textViewTypeActivity)
        val data: TextView = itemView.findViewById(R.id.textViewDate)
        val valor: TextView = itemView.findViewById(R.id.textViewPriceActivity)
        val signal: TextView = itemView.findViewById(R.id.textViewPosNeg)

        fun bind(activity: Activity) {
            nome.text = activity.name
            category.text = activity.category
            data.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(activity.date)
            valor.text = FormatBalance().format(activity.price)
        }
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
        val userController = UserController(holder.itemView.context)
        holder.itemView.setOnLongClickListener{
            holder.bind(atividades[position])
            if (position != RecyclerView.NO_POSITION) {
                userController.deleteActivity(atividades[position], holder.itemView.context)
                atividades.removeAt(position)
                notifyItemRemoved(position)
                onItemRemoved()
            }
            true
        }

    }

    override fun getItemCount() = atividades.size
}