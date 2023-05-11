package com.example.apedeapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CheckOutPage : AppCompatActivity() {

    private lateinit var cardName: EditText
    private lateinit var cardNumber:EditText
    private lateinit var cardExpire:EditText
    private lateinit var cardCVV:EditText

    private lateinit var textCardName: TextView
    private lateinit var textCardNumber:TextView
    private lateinit var textCardExpire:TextView
    private lateinit var textCardCVV:TextView

    private lateinit var saveCard: Button
    private lateinit var deleteCard: Button
    private lateinit var updateCard: Button

    private lateinit var buttonPay: Button

    private lateinit var pb: ProgressBar

    private var db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out_page)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        cardName = findViewById(R.id.editTextTextPersonName21)
        cardNumber = findViewById(R.id.editTextTextPersonName25)
        cardExpire = findViewById(R.id.editTextTextPersonName23)
        cardCVV = findViewById(R.id.editTextTextPersonName24)

        textCardName = findViewById(R.id.cardName)
        textCardNumber = findViewById(R.id.number)
        textCardExpire = findViewById(R.id.ex)
        textCardCVV = findViewById(R.id.cvv)

        pb = findViewById(R.id.progressBar5)

        saveCard = findViewById(R.id.button19)
        deleteCard = findViewById(R.id.button2)
        updateCard = findViewById(R.id.button5)

        buttonPay = findViewById(R.id.button)

        pb.visibility = View.INVISIBLE

        val t = intent.getStringExtra("totalPrice").toString().toEditable()


        saveCard.setOnClickListener {

            val userID = auth.currentUser?.uid.toString()

            val eCardName = cardName.text.toString().trim()
            val eCardNumber = cardNumber.text.toString().trim()
            val eCardEx = cardExpire.text.toString().trim()
            val eCardCVV = cardCVV.text.toString().trim()

            val cardMap = hashMapOf(
                "userID" to userID,
                "cardNumber" to eCardNumber,
                "cardName" to eCardName,
                "cardEx" to eCardEx,
                "cardCVV" to eCardCVV
            )

            db.collection("payments").document(userID).collection("userCardDetails").document(userID).set(cardMap)
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

        db.collection("payments").document(auth.currentUser!!.uid).collection("userCardDetails").document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {

                if (it != null) {
                    val textCardNumberU = it.data?.get("cardNumber")?.toString()
                    val textCardNameU = it.data?.get("cardName")?.toString()
                    val textCardExU = it.data?.get("cardEx")?.toString()
                    val textCardCVVU = it.data?.get("cardCVV")?.toString()


                    if (textCardNumberU != null) {
                        textCardNumber.text = textCardNumberU.toEditable()
                    }
                    if (textCardNameU != null) {
                        textCardName.text = textCardNameU.toEditable()
                    }
                    if (textCardExU != null) {
                        textCardExpire.text = textCardExU.toEditable()
                    }
                    if (textCardCVVU != null) {
                        textCardCVV.text = textCardCVVU.toEditable()
                    }


                }
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }


        deleteCard.setOnClickListener {
            val userID = auth.currentUser?.uid.toString()

            db.collection("payments").document(userID).collection("userCardDetails").document(userID)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this,"Deleted", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CheckOutPage::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener{
                    Toast.makeText(this,"Failed!", Toast.LENGTH_SHORT).show()
                }
        }

        updateCard.setOnClickListener {
            val intent = Intent(this, PaymentUpdate::class.java)
            this.startActivity(intent)
        }

        buttonPay.setOnClickListener {

            pb.visibility = View.VISIBLE

            if (textCardNumber.text == ""){
                Toast.makeText(this, "Please Add A Card", Toast.LENGTH_SHORT).show()
            }
            else{

                val userID = auth.currentUser?.uid.toString()

                pb.visibility = View.VISIBLE

                val db = FirebaseFirestore.getInstance()
                val parentDocRef = db.collection("cart").document(userID)
                val subCollectionRef = parentDocRef.collection("singleUser")

                subCollectionRef.get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot) {
                            document.reference.delete()

                            pb.visibility = View.INVISIBLE
                            Toast.makeText(this, "Payment Success!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, PaymentSuccess::class.java)
                            startActivity(intent)
                            finish()
                        }

                    }
                    .addOnFailureListener {
                        pb.visibility = View.INVISIBLE
                        Toast.makeText(this, "Payment Failed! Try Again!", Toast.LENGTH_SHORT).show()

                    }

            }
        }

    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}