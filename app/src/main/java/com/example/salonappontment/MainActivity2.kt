package com.example.salonappontment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity2 : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener{
            val intent = Intent(this, BookAppointment::class.java)
            startActivity(intent)
        }

        database = FirebaseDatabase.getInstance().reference
        recyclerView = findViewById(R.id.recyclerViewAppointments)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            // Fetch user's bookings from Firebase
            fetchBookings(userId)
        }
    }

    private fun fetchBookings(userId: String) {
        val userBookingsRef = database.child("user_bookings").child(userId)

        userBookingsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // User has bookings, display them
                    val bookingKeys = snapshot.children.mapNotNull { it.key }
                    displayBookings(bookingKeys)
                } else {
                    // User has no bookings, display a message
                    displayNoBookingsMessage()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun displayBookings(bookingKeys: List<String>) {
        val appointments = mutableListOf<Appointment>()

        // Use a counter to keep track of processed bookings
        var processedBookings = 0

        for (bookingKey in bookingKeys) {
            val bookingRef = database.child("bookings").child(bookingKey)
            bookingRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Convert the snapshot to an Appointment object
                        val appointment = snapshot.getValue(Appointment::class.java)
                        if (appointment != null) {
                            appointments.add(appointment)
                        }

                        // Check if all bookings have been processed
                        processedBookings++
                        if (processedBookings == bookingKeys.size) {
                            // All bookings processed, update UI
                            updateUIWithAppointments(appointments)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    processedBookings++
                    if (processedBookings == bookingKeys.size) {
                        // All bookings processed, update UI
                        updateUIWithAppointments(appointments)
                    }
                }
            })
        }
    }

    private fun updateUIWithAppointments(appointments: List<Appointment>) {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = AppointmentsAdapter(appointments)
    }

    private fun displayNoBookingsMessage() {
        // Display a message indicating that no bookings have been made
        // You can customize this message or handle it according to your UI
        // For now, let's just log a message
        println("No bookings made")
    }
}
