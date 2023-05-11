package com.example.apedeapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UpdateSellerItem : AppCompatActivity() {

    private lateinit var itemName: EditText
    private lateinit var colour: EditText
    private lateinit var price: EditText
    private lateinit var description: EditText
    private lateinit var randomID:TextView
    private lateinit var itemID: TextView

    private lateinit var updateItemBTN: Button

    private var database = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var image:ImageView

    private lateinit var pb: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_seller_item)

        itemName = findViewById(R.id.editTextTextPersonName13)
        colour = findViewById(R.id.editTextTextPersonName14)
        price = findViewById(R.id.editTextTextPersonName15)
        description = findViewById(R.id.editTextTextPersonName16)
        randomID = findViewById(R.id.textView7)
        image = findViewById(R.id.imageView6)
        itemID = findViewById(R.id.textView73)

        updateItemBTN = findViewById(R.id.button13)

        pb = findViewById(R.id.progressBar2)

        auth = FirebaseAuth.getInstance()

        pb.visibility = View.INVISIBLE

        randomID.text = intent.getStringExtra("randomID").toString().toEditable()
        itemID.text = intent.getStringExtra("itemID").toString().toEditable()
        itemName.text = intent.getStringExtra("itemName").toString().toEditable()
        price.text = intent.getStringExtra("itemPrice").toString().toEditable()
        colour.text = intent.getStringExtra("itemColor").toString().toEditable()
        description.text = intent.getStringExtra("itemDescription").toString().toEditable()
        val imageUrl = intent.getStringExtra("image")
        Glide.with(this)
            .load(imageUrl)
            .into(image)


        updateItemBTN.setOnClickListener {

            pb.visibility = View.VISIBLE

            val eItemName = itemName.text.toString().trim()
            val eItemPrice = price.text.toString().trim()
            val eItemDescription = description.text.toString().trim()
            val eItemColor = colour.text.toString().trim()


            val itemMapUpdated = mapOf(
                "itemName" to eItemName,
                "ItemPrice" to eItemPrice,
                "ItemDescription" to eItemDescription,
                "itemColor" to eItemColor
            )

            database.collection("sellerItems").document(intent.getStringExtra("randomID").toString())
                .update(itemMapUpdated)
            database.collection("sellerItemsBySellerID")
                .document(intent.getStringExtra("itemID").toString())
                .collection("singleSellerItems")
                .document(intent.getStringExtra("randomID").toString()).update(itemMapUpdated)
                .addOnSuccessListener {
                    pb.visibility = View.INVISIBLE
                    Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT)
                        .show()

                    val i = Intent(this, StoreView::class.java)
                    startActivity(i)
                    finish()

                }
                .addOnFailureListener {
                    pb.visibility = View.INVISIBLE
                    Toast.makeText(this, "Seller update fail", Toast.LENGTH_SHORT).show()

                }
        }
    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}