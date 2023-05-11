package com.example.apedeapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class StoreView : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sellerList: ArrayList<Store>

    private lateinit var sellerID: TextView
    private lateinit var shopName: TextView
    private lateinit var shopAddress: TextView
    private lateinit var shopContactNumber: TextView
    private lateinit var addItemBtn: TextView

    private lateinit var editStoreBtn: ImageButton
    private lateinit var deleteStoreBtn: ImageButton

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser

    private var database = Firebase.firestore

    private lateinit var logOut: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_view)

        recyclerView= findViewById(R.id.recycler_seller)

        auth = FirebaseAuth.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        sellerID = findViewById(R.id.textView12)
        shopName = findViewById(R.id.textView)
        shopAddress = findViewById(R.id.textView13)
        shopContactNumber = findViewById(R.id.textView14)
        addItemBtn = findViewById(R.id.textView6)

        logOut = findViewById(R.id.textView22)

        editStoreBtn = findViewById(R.id.imageButton2)
        deleteStoreBtn = findViewById(R.id.imageButton)

        val singleSellerID = auth.currentUser?.uid.toString()

        recyclerView.layoutManager= LinearLayoutManager(this)

        sellerList = arrayListOf()

        database = FirebaseFirestore.getInstance()

        database.collection("sellerStore").document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                sellerID.text = auth.currentUser!!.uid

                if (it != null) {
                    val eStoreName = it.data?.get("shopName")?.toString()
                    val eStoreAddress = it.data?.get("shopAddress")?.toString()
                    val eStoreContact = it.data?.get("shopContact")?.toString()

                    if (eStoreName != null) {
                        shopName.text = eStoreName.toEditable()
                    }
                    if (eStoreAddress != null) {
                        shopAddress.text = eStoreAddress.toEditable()
                    }
                    if (eStoreContact != null) {
                        shopContactNumber.text = eStoreContact.toEditable()
                    }

                }
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }

        database.collection("sellerItemsBySellerID").document(singleSellerID).collection("singleSellerItems").get()


            .addOnSuccessListener {
                for (data in it.documents){
                    val store:Store? = data.toObject(Store::class.java)
                    if (store != null) {
                        sellerList.add(store)
                    }
                }
                recyclerView.adapter =StoreAdapter(sellerList,this,database)


                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, it.toString() , Toast.LENGTH_SHORT).show()
            }

        editStoreBtn.setOnClickListener {
            val intent = Intent(this, EditStore::class.java)
            this.startActivity(intent)
        }

        addItemBtn.setOnClickListener{
            val intent = Intent(this, AddNewItem::class.java)
            this.startActivity(intent)
        }

        deleteStoreBtn.setOnClickListener {
            database.collection("sellerStore").document(auth.currentUser!!.uid)
                .delete()
                firebaseUser.delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Deleted Store Success!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CreateStore::class.java)
                    this.startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Delete Failed!", Toast.LENGTH_SHORT).show()
                }
        }

        logOut.setOnClickListener{
            val intent = Intent(this, HelloYou::class.java)
            this.startActivity(intent)
            finish()
        }

    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}