package com.example.myshopinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun getShopList() : LiveData<List<ShopItem>>
    suspend fun getShopItem(shopItemId : Int) : ShopItem
    suspend fun addShopItem(item : ShopItem)
    suspend fun deleteShopItem(item : ShopItem)
    suspend fun editShopItem(item : ShopItem)

}