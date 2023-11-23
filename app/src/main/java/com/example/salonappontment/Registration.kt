package com.example.salonappontment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase

class Registration : AppCompatActivity() {

    data class User(
        val name: String,
        val phone: String,
        val email: String,
        val password: String
    )
    private lateinit var auth: FirebaseAuth
    private lateinit var eTEmail: EditText
    private lateinit var eTPassword: EditText
    private lateinit var eTName: EditText
    private lateinit var eTPhone: EditText
    private lateinit var btnRegisterUser: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = Firebase.auth
        eTName = findViewById(R.id.eTName)
        eTPhone = findViewById(R.id.eTPhone)
        eTEmail = findViewById(R.id.eTEmail)
        eTPassword = findViewById(R.id.eTPassword)
        btnRegisterUser = findViewById(R.id.btnRegisterUser)

        btnRegisterUser.setOnClickListener{
            val name = eTName.text.toString()
            val phone = eTPhone.text.toString()
            val email = eTEmail.text.toString()
            val password = eTPassword.text.toString()
                register(name, phone, email, password)

        }
    }
    private fun validateInputs(name: String, phone: String, email: String, password: String): Boolean {
        return name.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
    }
    private fun register(name: String, phone: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener (this) {
                task ->
                if (task.isSuccessful){
                    val ref=FirebaseDatabase.getInstance().reference.child("users")
                    ref.child(task.result.user!!.uid).setValue(User(name,phone,email,password)).addOnCompleteListener {
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    }
                else{
                    Toast.makeText(baseContext, "Registration failed. Try again.${task.exception?.message}", Toast.LENGTH_SHORT).show()

                    }
                }
            }
    }


