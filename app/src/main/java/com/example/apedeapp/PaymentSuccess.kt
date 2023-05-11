package com.example.apedeapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class PaymentSuccess : AppCompatActivity() {

    // Declare a lateinit variable called "ok" of type Button
    private lateinit var ok: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for this activity to the "activity_payment_success.xml" layout file
        setContentView(R.layout.activity_payment_success)

        // Initialize the "ok" button by finding it using its ID
        ok = findViewById(R.id.button3)

        // Initialize the "ok" button by finding it using its ID
        ok.setOnClickListener {

            // Create an intent to go to the BuyerDashboard activity
            val intent = Intent(this, BuyerDashboard::class.java)
            // Start the BuyerDashboard activity
            this.startActivity(intent)
        }
    }
}