package com.example.retrofitcrudapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProfesorAdapter(private val profesores: List<Profesor>) : RecyclerView.Adapter<ProfesorAdapter.ViewHolder>() {

    private var onItemClick: OnItemClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreTextView: TextView = view.findViewById(R.id.tvNombreProfesor)
        val apellidoTextView: TextView = view.findViewById(R.id.tvApellidoProfesor)
        val edadTextView: TextView = view.findViewById(R.id.tvEdadProfesor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profesor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profesor = profesores[position]
        holder.nombreTextView.text = profesor.nombre
        holder.apellidoTextView.text = profesor.apellido
        holder.edadTextView.text = profesor.edad.toString()

        holder.itemView.setOnClickListener {
            onItemClick?.onItemClick(profesor)
        }
    }

    override fun getItemCount(): Int {
        return profesores.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClick = listener
    }

    interface OnItemClickListener {
        fun onItemClick(profesor: Profesor)
    }
}
