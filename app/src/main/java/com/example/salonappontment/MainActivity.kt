package com.example.salonappontment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var eTEmail: EditText
    private lateinit var eTPassword: EditText
    private lateinit var btnLogin: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        val btn = findViewById<Button>(R.id.btnRegister)
        btn .setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }
        
        eTEmail = findViewById(R.id.eTEmail)
        eTPassword = findViewById(R.id.eTPassword)
        btnLogin = findViewById(R.id.btnLogin)
        
        btnLogin.setOnClickListener {
            val email = eTEmail.text.toString()
            val password = eTPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){
               login(email, password)
                }else{
                    Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun login(email: String, password: String) {
               auth.signInWithEmailAndPassword(email,password)
                   .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            if(auth.currentUser!!.uid.toString()!="")
                            {
                                // Login successful
                                Toast.makeText(baseContext, "Login successful", Toast.LENGTH_SHORT).show()

                                // Redirect to the mainMenu activity
                                val intent = Intent(this, MainActivity2::class.java)
                                startActivity(intent)
                                finish()
                            }


            } else {
                Toast.makeText(baseContext, "Authentication failed+${task.exception.toString()}", Toast.LENGTH_SHORT).show()

            }
        }
}}