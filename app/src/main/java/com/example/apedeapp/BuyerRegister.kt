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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BuyerRegister : AppCompatActivity() {

    private lateinit var buyerGmail: EditText
    private lateinit var buyerPassword: EditText

    private lateinit var createAccount: AppCompatButton
    private lateinit var tvNavigateToSignIn: TextView

    private lateinit var pb: ProgressBar

    private lateinit var auth: FirebaseAuth

    private var database = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_register)

        auth = FirebaseAuth.getInstance()

        buyerGmail = findViewById(R.id.etEmailSignUp)
        buyerPassword = findViewById(R.id.etPasswordSignUp)
        tvNavigateToSignIn = findViewById(R.id.tvNavigateToSignIn)

        createAccount = findViewById(R.id.btnSignUp)

        pb = findViewById(R.id.progressBar3)

        pb.visibility = View.INVISIBLE

        createAccount.setOnClickListener {

            pb.visibility = View.VISIBLE

            var eSignupEmail = buyerGmail.text.toString().trim()
            var eSignupPassword = buyerPassword.text.toString().trim()

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

                        val intent = Intent(this, BuyerLogin::class.java)
                        startActivity(intent)
                        finish()


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

        tvNavigateToSignIn.setOnClickListener {
            val intent = Intent(this, BuyerLogin::class.java)
            startActivity(intent)
            finish()
        }
    }
}