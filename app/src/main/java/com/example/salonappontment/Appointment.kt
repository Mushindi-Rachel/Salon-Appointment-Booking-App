package com.example.salonappontment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Appointment(
    val services: List<String>? = null,
    val date: String? = null,
    val time: String? = null,
    val numberOfPeople: Int? = null
)


class AppointmentsAdapter(private val appointments: List<Appointment>) :
    RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val servicesTView: TextView = itemView.findViewById(R.id.servicesTView)
        val dateTView: TextView = itemView.findViewById(R.id.dateTView)
        val timeTView: TextView = itemView.findViewById(R.id.timeTView)
        val numberOfPeopleTView: TextView = itemView.findViewById(R.id.numberOfPeopleTView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.appointment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.servicesTView.text = "Services: ${appointment.services}"
        holder.dateTView.text = "Date: ${appointment.date}"
        holder.timeTView.text = "Time: ${appointment.time}"
        holder.numberOfPeopleTView.text = "Number of People: ${appointment.numberOfPeople}"
    }

    override fun getItemCount(): Int {
        return appointments.size
    }
}
