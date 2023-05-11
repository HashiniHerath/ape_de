package com.example.apedeapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class StoreAdapter (
    private val storeItemsList: ArrayList<Store>,
    private val context: Context,
    private val db: FirebaseFirestore
) : RecyclerView.Adapter<StoreAdapter.StoreViewHolder>()  {

    class StoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sellerID: TextView = itemView.findViewById(R.id.sellerId)
        val itemTitle: TextView = itemView.findViewById(R.id.selItemTitle)
        val itemPrice: TextView = itemView.findViewById(R.id.price)
        val itemDescription: TextView = itemView.findViewById(R.id.desription)
        val itemColor: TextView = itemView.findViewById(R.id.color)
        val randomID: TextView = itemView.findViewById(R.id.randomId)
        val image: ImageView = itemView.findViewById(R.id.imageView3)

        val updateBtn: Button = itemView.findViewById(R.id.button5)
        val deleteBtn: Button = itemView.findViewById(R.id.button2)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.single_item_to_seller,parent,false)
        return StoreViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.sellerID.text = storeItemsList[position].sellerID
        holder.itemTitle.text = storeItemsList[position].itemName
        holder.itemPrice.text = storeItemsList[position].ItemPrice
        holder.itemDescription.text = storeItemsList[position].ItemDescription
        holder.itemColor.text = storeItemsList[position].itemColor
        holder.randomID.text = storeItemsList[position].randomID
        Glide.with(context).load(storeItemsList[position].url).into(holder.image)

        holder.updateBtn.setOnClickListener(){
            val intent = Intent(context, UpdateSellerItem::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("image", storeItemsList[position].url)
            intent.putExtra("itemID", storeItemsList[position].sellerID)
            intent.putExtra("randomID", storeItemsList[position].randomID)
            intent.putExtra("itemPrice", storeItemsList[position].ItemPrice)
            intent.putExtra("itemName", storeItemsList[position].itemName)
            intent.putExtra("itemColor", storeItemsList[position].itemColor)
            intent.putExtra("itemDescription", storeItemsList[position].ItemDescription)
            context.startActivity(intent)
        }

        holder.deleteBtn.setOnClickListener(){
            storeItemsList[position].randomID?.let { it1 -> storeItemsList[position].sellerID?.let { it2 ->
                deleteSellItem(it1,position,
                    it2
                )
            } }
        }

    }

    override fun getItemCount(): Int {
        return storeItemsList.size
    }

    private fun deleteSellItem(randomID: String, position: Int, sellerID:String) {

        db.collection("sellerItemsBySellerID").document(sellerID).collection("singleSellerItems")
            .document(randomID)
            .delete()

        db.collection("sellerItems")
            .document(randomID)
            .delete()
            .addOnCompleteListener {
                storeItemsList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, storeItemsList.size)
                Toast.makeText(context, "Item has been deleted!", Toast.LENGTH_SHORT).show()
            }
    }
}