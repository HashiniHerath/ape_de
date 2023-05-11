package com.example.apedeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HelloSeller : AppCompatActivity() {

    private lateinit var createStore: Button
    private lateinit var loginToStore: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello_seller)

        createStore = findViewById(R.id.button5)
        loginToStore = findViewById(R.id.button6)

        createStore.setOnClickListener {
            val intent = Intent(this, CreateStore::class.java)
            startActivity(intent)
        }

        loginToStore.setOnClickListener {
            val intent = Intent(this, SellerLogin::class.java)
            startActivity(intent)
        }
    }
}