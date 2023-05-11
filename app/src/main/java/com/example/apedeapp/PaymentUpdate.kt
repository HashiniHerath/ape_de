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

    private lateinit var cardName: EditText
    private lateinit var cardNumber: EditText
    private lateinit var cardExpire: EditText
    private lateinit var cardCVV: EditText

    private lateinit var updateCard: Button

    private var db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_update)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        cardName = findViewById(R.id.editTextTextPersonName21)
        cardNumber = findViewById(R.id.editTextTextPersonName25)
        cardExpire = findViewById(R.id.editTextTextPersonName23)
        cardCVV = findViewById(R.id.editTextTextPersonName24)

        updateCard = findViewById(R.id.button19)

        db.collection("payments").document(auth.currentUser!!.uid).collection("userCardDetails").document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {

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

        updateCard.setOnClickListener {

            val userID = auth.currentUser?.uid.toString()

            val eCardName = cardName.text.toString().trim()
            val eCardNumber = cardNumber.text.toString().trim()
            val eCardEx = cardExpire.text.toString().trim()
            val eCardCVV = cardCVV.text.toString().trim()

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