package com.example.myshopinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.myshopinglist.R
import com.example.myshopinglist.domain.ShopItem
import java.io.IOException

class ShopListAdapter : ListAdapter<ShopItem, ShopListHolder>(ShopItemDiffUtil()) {

/*    var listShop = listOf<ShopItem>()
    set(value) {
        val diff = ShopDiffUtil(listShop, value)
        val result = DiffUtil.calculateDiff(diff)
        result.dispatchUpdatesTo(this)
        field = value
    }*/
    var onShopItemLongClick : ((ShopItem) -> Unit)? = null
    var onClickListener : ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListHolder {
        val layout = when (viewType){
            VIEW_ENABLED -> R.layout.shop_item_enable
            VIEW_DISABLED -> R.layout.shop_item_disable
            else -> throw IOException("Error: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopListHolder(view)
    }

    override fun onBindViewHolder(holder: ShopListHolder, position: Int) {
        val shopItems : ShopItem = getItem(position)
        holder.tvName.text = shopItems.name
        holder.tvCount.text = shopItems.count.toString()
        holder.itemView.setOnLongClickListener {
            onShopItemLongClick?.invoke(shopItems)
            true

        }
        holder.itemView.setOnClickListener {
            onClickListener?.invoke(shopItems)
        }
    }

 /*   override fun getItemCount(): Int {
        return listShop.size
    }*/


    override fun getItemViewType(position: Int): Int {
        val shopItem = getItem(position)
        return if(shopItem.enabled){
            VIEW_ENABLED
        }else{
            VIEW_DISABLED
        }
    }

    companion object{
        const val VIEW_DISABLED = 100
        const val VIEW_ENABLED = 101
        const val MAX_POOL_SIZE = 10
    }
}