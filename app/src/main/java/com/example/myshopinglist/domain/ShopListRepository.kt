package com.example.myshopinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun getShopList() : LiveData<List<ShopItem>>
    fun getShopItem(shopItemId : Int) : ShopItem
    fun addShopItem(item : ShopItem)
    fun deleteShopItem(item : ShopItem)
    fun editShopItem(item : ShopItem)

}