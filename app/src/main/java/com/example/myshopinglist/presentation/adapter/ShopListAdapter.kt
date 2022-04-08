package com.example.myshopinglist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.example.myshopinglist.R
import com.example.myshopinglist.databinding.ShopItemDisableBinding
import com.example.myshopinglist.databinding.ShopItemEnableBinding
import com.example.myshopinglist.domain.ShopItem
import com.example.myshopinglist.presentation.ShopItemDiffUtil
import java.io.IOException
import java.util.zip.Inflater

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
        val bind = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), layout, parent, false)
        return ShopListHolder(bind)
    }

    override fun onBindViewHolder(holder: ShopListHolder, position: Int) {
        val shopItems : ShopItem = getItem(position)
        val binding = holder.bind
        when(binding){
            is ShopItemDisableBinding -> {
                binding.shopItem = shopItems
            }
            is ShopItemEnableBinding ->{
                binding.shopItem = shopItems
            }
        }

        binding.root.setOnLongClickListener {
            onShopItemLongClick?.invoke(shopItems)
            true

        }
        binding.root.setOnClickListener {
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