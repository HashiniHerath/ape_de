package com.example.apedeapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class HelloYou : AppCompatActivity() {

    private lateinit var toBuyerLogin: Button
    private lateinit var toBuyerRegister: Button
    private lateinit var becomeASeller: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello_you)

        toBuyerLogin = findViewById(R.id.button3)
        toBuyerRegister = findViewById(R.id.button4)
        becomeASeller = findViewById(R.id.textView19)

        toBuyerLogin.setOnClickListener {
            val intent = Intent(this, BuyerLogin::class.java)
            startActivity(intent)
        }

        toBuyerRegister.setOnClickListener {
            val intent = Intent(this, BuyerRegister::class.java)
            startActivity(intent)
        }

        becomeASeller.setOnClickListener{
            val intent = Intent(this, HelloSeller::class.java)
            startActivity(intent)
        }
    }
}