package com.example.apedeapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PaymentUpdate : AppCompatActivity() {

    // Declare EditText and Button variables
    private lateinit var cardName: EditText
    private lateinit var cardNumber: EditText
    private lateinit var cardExpire: EditText
    private lateinit var cardCVV: EditText

    private lateinit var updateCard: Button

    // Initialize Firebase Firestore and Firebase Auth
    private var db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_update)

        // Get instance of Firebase Firestore and Firebase Auth
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Assign the EditText and Button variables to their corresponding views
        cardName = findViewById(R.id.editTextTextPersonName21)
        cardNumber = findViewById(R.id.editTextTextPersonName25)
        cardExpire = findViewById(R.id.editTextTextPersonName23)
        cardCVV = findViewById(R.id.editTextTextPersonName24)

        updateCard = findViewById(R.id.button19)

        // Retrieve user's card details from Firebase Firestore
        db.collection("payments").document(auth.currentUser!!.uid).collection("userCardDetails").document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {

                // If user's card details exist in Firebase Firestore, retrieve the card details and populate the EditText fields
                if (it != null) {
                    val textCardNumberU = it.data?.get("cardNumber")?.toString()
                    val textCardNameU = it.data?.get("cardName")?.toString()
                    val textCardExU = it.data?.get("cardEx")?.toString()
                    val textCardCVVU = it.data?.get("cardCVV")?.toString()


                    if (textCardNumberU != null) {
                        cardName.text = textCardNumberU.toEditable()
                    }
                    if (textCardNameU != null) {
                        cardNumber.text = textCardNameU.toEditable()
                    }
                    if (textCardExU != null) {
                        cardExpire.text = textCardExU.toEditable()
                    }
                    if (textCardCVVU != null) {
                        cardCVV.text = textCardCVVU.toEditable()
                    }


                }
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }

        // Set OnClickListener for the updateCard button to update user's card details in Firebase Firestore
        updateCard.setOnClickListener {

            // Retrieve user's ID and the card details entered by the user

            val userID = auth.currentUser?.uid.toString()
            val eCardName = cardName.text.toString().trim()
            val eCardNumber = cardNumber.text.toString().trim()
            val eCardEx = cardExpire.text.toString().trim()
            val eCardCVV = cardCVV.text.toString().trim()

            // Create a map of the card details to update in Firebase Firestore
            val cardUpdateMap = mapOf(
                "cardNumber" to eCardNumber,
                "cardName" to eCardName,
                "cardEx" to eCardEx,
                "cardCVV" to eCardCVV
            )

            db.collection("payments").document(userID).collection("userCardDetails").document(userID).update(cardUpdateMap)
                .addOnSuccessListener {
                    Toast.makeText(this,"Added", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CheckOutPage::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener{
                    Toast.makeText(this,"Failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }
    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}