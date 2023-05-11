package com.example.apedeapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class PaymentSuccess : AppCompatActivity() {

    private lateinit var ok: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        ok = findViewById(R.id.button3)

        ok.setOnClickListener {
            val intent = Intent(this, BuyerDashboard::class.java)
            this.startActivity(intent)
        }
    }
}