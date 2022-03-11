package com.example.myshopinglist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myshopinglist.R
import com.example.myshopinglist.domain.ShopItem
import kotlin.random.Random

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopListHolder>() {

    private val listShop = mutableListOf<ShopItem>()
    init {
        for (i in 1..100)
        {
            listShop.add(ShopItem("Name № $i", i, Random.nextBoolean()))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shop_item_disable, parent, false)
        return ShopListHolder(view)
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ShopListHolder, position: Int) {
        val shopItems : ShopItem = listShop[position]
        holder.tvName.text = shopItems.name
        holder.tvCount.text = shopItems.count.toString()
    }

    override fun getItemCount(): Int {
        return listShop.size
    }

    class ShopListHolder(view : View) : RecyclerView.ViewHolder(view){
        val tvName : TextView = view.findViewById(R.id.tvName)
        val tvCount : TextView = view.findViewById(R.id.tvCount)
    }
}