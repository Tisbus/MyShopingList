package com.example.myshopinglist.domain

interface ShopListRepository {

    fun getShopList() : List<ShopItem>
    fun getShopItem(shopItemId : Int) : ShopItem
    fun addShopItem(item : ShopItem)
    fun deleteShopItem(item : ShopItem)
    fun editShopItem(item : ShopItem)

}