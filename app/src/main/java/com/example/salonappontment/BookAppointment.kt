package com.example.salonappontment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BookAppointment : AppCompatActivity() {
    data class Appointment(
        val service1: Boolean,
        val service2: Boolean,
        val service3: Boolean,
        val service4: Boolean,
        val service5: Boolean,
        val service6: Boolean,
        val service7: Boolean,
        val date: String,
        val time: String,
        val numberOfPeople: Int
    )

    private lateinit var database: DatabaseReference
    private lateinit var checkBoxService1: CheckBox
    private lateinit var checkBoxService2: CheckBox
    private lateinit var checkBoxService3: CheckBox
    private lateinit var checkBoxService4: CheckBox
    private lateinit var checkBoxService5: CheckBox
    private lateinit var checkBoxService6: CheckBox
    private lateinit var checkBoxService7: CheckBox
    private lateinit var eTDate: EditText
    private lateinit var editTextTime: EditText
    private lateinit var eTNumber: EditText
    private lateinit var btnBook: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)

        database = FirebaseDatabase.getInstance().reference

        checkBoxService1 = findViewById(R.id.Service1)
        checkBoxService2 = findViewById(R.id.Service2)
        checkBoxService3 = findViewById(R.id.Service3)
        checkBoxService4 = findViewById(R.id.Service4)
        checkBoxService5 = findViewById(R.id.Service5)
        checkBoxService6 = findViewById(R.id.Service6)
        checkBoxService7 = findViewById(R.id.Service7)
        eTDate = findViewById(R.id.eTDate)
        editTextTime = findViewById(R.id.editTextTime)
        eTNumber = findViewById(R.id.eTNumber)
        btnBook = findViewById(R.id.btnBook)

        btnBook.setOnClickListener {
            val selectedServices = mutableListOf<String>()

            if (checkBoxService1.isChecked) {
                selectedServices.add("Hair-cutting, colouring and styling")
            }
            if (checkBoxService2.isChecked) {
                selectedServices.add("Waxing and other forms of hair removal")
            }
            if (checkBoxService3.isChecked) {
                selectedServices.add("Nail treatment")
            }
            if (checkBoxService4.isChecked) {
                selectedServices.add("Facial and skin care treatments")
            }
            if (checkBoxService5.isChecked) {
                selectedServices.add("Massage")
            }
            if (checkBoxService6.isChecked) {
                selectedServices.add("Tanning")
            }
            if (checkBoxService7.isChecked) {
                selectedServices.add("Aromatherapy")
            }

            val selectedDate = eTDate.text.toString()
            val selectedTime = editTextTime.text.toString()
            val numberOfPeople = eTNumber.text.toString().toIntOrNull() ?: 0

            saveServicesToFirebase(selectedServices, selectedDate, selectedTime, numberOfPeople)
        }
    }

    private fun saveServicesToFirebase(
        services: List<String>,
        selectedDate: String,
        selectedTime: String,
        numberOfPeople: Int
    ) {
        val bookingKey = database.child("bookings").push().key

        val bookingData = HashMap<String, Any>()
        bookingData["services"] = services  // Store services as a list of strings
        bookingData["date"] = selectedDate
        bookingData["time"] = selectedTime
        bookingData["numberOfPeople"] = numberOfPeople

        if (bookingKey != null) {
            database.child("bookings").child(bookingKey).setValue(bookingData)
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                database.child("user_bookings").child(userId).child(bookingKey).setValue(true)
                Toast.makeText(this, "Booking made successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            Log.e("BookAppointment", "Error generating booking key.")
        }
    }
}