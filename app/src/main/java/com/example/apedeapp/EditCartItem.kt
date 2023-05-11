package com.example.apedeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditCartItem : AppCompatActivity() {

    private lateinit var itemName: TextView
    private lateinit var quantity: TextView
    private lateinit var bottomPrice: TextView
    private lateinit var itemID: TextView

    private lateinit var plusQuantity: ImageButton
    private lateinit var minusQuantity: ImageButton

    private lateinit var updateItemBTN: Button

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cart_item)

        itemName = findViewById(R.id.textView3)
        quantity = findViewById(R.id.editTextTextPersonName14)
        bottomPrice = findViewById(R.id.textView9)
        itemID = findViewById(R.id.itemIDCart)

        auth = FirebaseAuth.getInstance()

        plusQuantity = findViewById(R.id.imageButton4)
        minusQuantity = findViewById(R.id.imageButton3)

        updateItemBTN = findViewById(R.id.textView12)

        quantity.text = intent.getStringExtra("cartItemQuantity").toString().toEditable()
        bottomPrice.text = intent.getStringExtra("cartItemPrice").toString().toEditable()
        itemName.text = intent.getStringExtra("cartItemTitle").toString().toEditable()
        itemID.text = intent.getStringExtra("cartItemID").toString()

        val userID = auth.currentUser?.uid.toString()

        val b1Price: Int = bottomPrice.text.toString().trim().toInt()
        val b2Price: Int = quantity.text.toString().trim().toInt()

        val botPrice: Int = b1Price / b2Price

        plusQuantity.setOnClickListener{

            val yourInteger: Int = quantity.text.toString().trim().toInt()
            println(yourInteger)

            val yourIntegerPrice: Int = bottomPrice.text.toString().trim().toInt()
            println(yourIntegerPrice)

            quantity.text = (yourInteger + 1).toString().toEditable()

            bottomPrice.text = (yourIntegerPrice + botPrice).toString()
        }

        minusQuantity.setOnClickListener{

            val yourInteger: Int = quantity.text.toString().trim().toInt()
            println(yourInteger)

            val yourIntegerPrice: Int = bottomPrice.text.toString().trim().toInt()
            println(yourIntegerPrice)

            if (yourInteger == 1 || yourIntegerPrice == botPrice){
                Toast.makeText(this, "Check", Toast.LENGTH_SHORT).show()
            }else{

                quantity.text = (yourInteger -1).toString().toEditable()

                bottomPrice.text = (yourIntegerPrice - botPrice).toString()
            }
        }

        updateItemBTN.setOnClickListener {

            val eItemName = itemName.text.toString().trim()
            val eTopPrice = bottomPrice.text.toString().trim()
            val eQuantity = quantity.text.toString().trim()

            val cartMap = mapOf(
                "cartItemName" to eItemName,
                "cartItemPrice" to eTopPrice,
                "cartItemQuantity" to eQuantity
            )

            db.collection("cart").document(userID).collection("singleUser").document(intent.getStringExtra("cartItemID").toString()).update(cartMap)
                .addOnSuccessListener {
                    Toast.makeText(this,"Update Cart Success!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CartView::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener{
                    Toast.makeText(this,"Update Cart Failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}