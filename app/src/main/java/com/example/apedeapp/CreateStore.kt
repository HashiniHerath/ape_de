package com.example.apedeapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateStore : AppCompatActivity() {

    private lateinit var shopName: EditText
    private lateinit var shopAddress: EditText
    private lateinit var shopContactNumber: EditText
    private lateinit var sellerGmail: EditText
    private lateinit var sellerPassword: EditText

    private lateinit var createStore: Button

    private lateinit var pb:ProgressBar

    private lateinit var auth: FirebaseAuth

    private var database = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_store)

        auth = FirebaseAuth.getInstance()

        shopName = findViewById(R.id.editTextTextPersonName)
        shopAddress = findViewById(R.id.editTextTextPersonName2)
        shopContactNumber = findViewById(R.id.editTextTextPersonName3)
        sellerGmail = findViewById(R.id.editTextTextPersonName4)
        sellerPassword = findViewById(R.id.editTextTextPersonName5)

        createStore = findViewById(R.id.button7)

        pb = findViewById(R.id.progressBar)

        pb.visibility = View.INVISIBLE

        createStore.setOnClickListener {

            pb.visibility = View.VISIBLE

            var eSignupEmail = sellerGmail.text.toString().trim()
            var eSignupPassword = sellerPassword.text.toString().trim()

            val eShopName = shopName.text.toString().trim()
            val eShopAddress = shopAddress.text.toString().trim()
            val eShopContact = shopContactNumber.text.toString().trim()

            if (TextUtils.isEmpty(eSignupEmail)){
                Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show()
            }

            if (TextUtils.isEmpty(eSignupPassword)){
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show()
            }

            auth.createUserWithEmailAndPassword(eSignupEmail, eSignupPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "createUserWithEmail:success")
                        pb.visibility = View.INVISIBLE
                        val user = auth.currentUser

                        val sellerID = auth.currentUser?.uid.toString()

                        val shopMap = hashMapOf(
                            "sellerID" to sellerID,
                            "shopName" to eShopName,
                            "shopAddress" to eShopAddress,
                            "shopContact" to eShopContact
                        )

                        database.collection("sellerStore").document(sellerID).set(shopMap)
                            .addOnSuccessListener {
                                val intent = Intent(this, SellerLogin::class.java)
                                startActivity(intent)
                                finish()
                            }

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                        pb.visibility = View.INVISIBLE
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }
        }


    }
}