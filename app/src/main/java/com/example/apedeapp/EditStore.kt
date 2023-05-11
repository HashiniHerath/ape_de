package com.example.apedeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditStore : AppCompatActivity() {

    private lateinit var shopNameEdit: EditText
    private lateinit var shopAddressEdit: EditText
    private lateinit var shopContactNumberEdit: EditText

    private lateinit var saveStore: Button

    private lateinit var pb: ProgressBar

    private lateinit var auth: FirebaseAuth

    private var database = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_store)

        auth = FirebaseAuth.getInstance()

        shopNameEdit = findViewById(R.id.editTextTextPersonNameEdit)
        shopAddressEdit = findViewById(R.id.editTextTextPersonName2Edit)
        shopContactNumberEdit = findViewById(R.id.editTextTextPersonName3Edit)

        saveStore = findViewById(R.id.button7)

        pb = findViewById(R.id.progressBar)

        pb.visibility = View.INVISIBLE

        database.collection("sellerStore").document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {

                if (it != null) {
                    val eStoreName = it.data?.get("shopName")?.toString()
                    val eStoreAddress = it.data?.get("shopAddress")?.toString()
                    val eStoreContact = it.data?.get("shopContact")?.toString()

                    if (eStoreName != null) {
                        shopNameEdit.text = eStoreName.toEditable()
                    }
                    if (eStoreAddress != null) {
                        shopAddressEdit.text = eStoreAddress.toEditable()
                    }
                    if (eStoreContact != null) {
                        shopContactNumberEdit.text = eStoreContact.toEditable()
                    }

                }
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }

        saveStore.setOnClickListener {

            pb.visibility = View.VISIBLE

            val sellerID = auth.currentUser?.uid.toString()
            val eShopName = shopNameEdit.text.toString().trim()
            val eShopAddress = shopAddressEdit.text.toString().trim()
            val eShopContact = shopContactNumberEdit.text.toString().trim()

            val shopUpdateMap = mapOf(
                "shopName" to eShopName,
                "shopAddress" to eShopAddress,
                "shopContact" to eShopContact
            )

            database.collection("sellerStore").document(sellerID).update(shopUpdateMap)
                .addOnSuccessListener {

                    pb.visibility = View.INVISIBLE

                    Toast.makeText(this, "Update Success!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, StoreView::class.java)
                    startActivity(intent)
                    finish()
                }
        }
    }
    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}