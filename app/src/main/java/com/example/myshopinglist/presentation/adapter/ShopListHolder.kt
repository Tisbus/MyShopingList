package com.example.myshopinglist.presentation.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myshopinglist.R

class ShopListHolder(view : View) : RecyclerView.ViewHolder(view){
    val tvName : TextView = view.findViewById(R.id.tvName)
    val tvCount : TextView = view.findViewById(R.id.tvCount)
}