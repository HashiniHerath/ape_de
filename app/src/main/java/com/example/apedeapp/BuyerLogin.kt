package com.example.apedeapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth

class BuyerLogin : AppCompatActivity() {

    private lateinit var buyerGmail: EditText
    private lateinit var buyerPassword: EditText

    private lateinit var buyerLogin: AppCompatButton
    private lateinit var tvNavigateToSignUp: TextView

    private lateinit var pb: ProgressBar

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_login)

        auth = FirebaseAuth.getInstance()

        buyerGmail = findViewById(R.id.etEmailSignIn)
        buyerPassword = findViewById(R.id.etPasswordSignIn)
        tvNavigateToSignUp = findViewById(R.id.tvNavigateToSignUp)

        buyerLogin = findViewById(R.id.btnSignIn)

        pb = findViewById(R.id.progressBar4)

        pb.visibility = View.INVISIBLE

        buyerLogin.setOnClickListener {

            pb.visibility = View.VISIBLE

            var eSignInEmail = buyerGmail.text.toString().trim()
            var eSignInPassword = buyerPassword.text.toString().trim()

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
                        startActivity(Intent(this,BuyerDashboard::class.java))
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
        tvNavigateToSignUp.setOnClickListener {
            val intent = Intent(this, BuyerRegister::class.java)
            startActivity(intent)
            finish()
        }
    }
}