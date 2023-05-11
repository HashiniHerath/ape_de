package com.example.apedeapp

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

class SellerLogin : AppCompatActivity() {

    private lateinit var sellerGmail: EditText
    private lateinit var sellerPassword: EditText
    private lateinit var sellerLogin: Button

    private lateinit var pb: ProgressBar

    private lateinit var auth: FirebaseAuth

    private var database = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_login)

        auth = FirebaseAuth.getInstance()

        sellerGmail = findViewById(R.id.editTextTextPersonName4)
        sellerPassword = findViewById(R.id.editTextTextPersonName5)

        sellerLogin = findViewById(R.id.button7)

        pb = findViewById(R.id.progressBar)

        pb.visibility = View.INVISIBLE

        sellerLogin.setOnClickListener {

            pb.visibility = View.VISIBLE

            var eSignInEmail = sellerGmail.text.toString().trim()
            var eSignInPassword = sellerPassword.text.toString().trim()

            if (TextUtils.isEmpty(eSignInEmail)){
                Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show()
            }

            if (TextUtils.isEmpty(eSignInPassword)){
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show()
            }

            auth.signInWithEmailAndPassword(eSignInEmail, eSignInPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        pb.visibility = View.INVISIBLE
                        Toast.makeText(
                            baseContext,
                            "Authentication Success!",
                            Toast.LENGTH_SHORT,
                        ).show()
                        Log.d(ContentValues.TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        startActivity(Intent(this,StoreView::class.java))
                        finish()

                    } else {
                        // If sign in fails, display a message to the user.
                        pb.visibility = View.INVISIBLE
                        Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed!",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }
        }
    }
}