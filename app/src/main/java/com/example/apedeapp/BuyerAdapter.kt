package com.example.apedeapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BuyerAdapter(
    private val buyerItemList: ArrayList<Buyer>,
    private val context: Context,
) : RecyclerView.Adapter<BuyerAdapter.BuyerViewHolder>()  {

    class BuyerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sellerID: TextView = itemView.findViewById(R.id.sellerId)
        val itemName: TextView = itemView.findViewById(R.id.selItemTitle)
        val itemPrice: TextView = itemView.findViewById(R.id.price)
        val itemDescription: TextView = itemView.findViewById(R.id.desription)
        val itemColor: TextView = itemView.findViewById(R.id.color)
        val image: ImageView = itemView.findViewById(R.id.imageView3)

        val addToCart : TextView = itemView.findViewById(R.id.textView11)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyerViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.single_item_to_buyer,parent,false)
        return BuyerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BuyerViewHolder, position: Int) {
        holder.sellerID.text = buyerItemList[position].sellerID
        holder.itemName.text = buyerItemList[position].itemName
        holder.itemPrice.text = buyerItemList[position].ItemPrice
        holder.itemColor.text = buyerItemList[position].itemColor
        holder.itemDescription.text = buyerItemList[position].ItemDescription
        Glide.with(context).load(buyerItemList[position].url).into(holder.image)

        holder.addToCart.setOnClickListener(){
            val intent = Intent(context, AddToCart::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("image", buyerItemList[position].url)
            intent.putExtra("itemID", buyerItemList[position].sellerID)
            intent.putExtra("iName", buyerItemList[position].itemName)
            intent.putExtra("iPrice", buyerItemList[position].ItemPrice)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return buyerItemList.size
    }
}