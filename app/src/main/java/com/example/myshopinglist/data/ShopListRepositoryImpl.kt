package com.example.myshopinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myshopinglist.domain.ShopItem
import com.example.myshopinglist.domain.ShopListRepository
import java.lang.RuntimeException

object ShopListRepositoryImpl : ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()

    private var autoIncrementId = 0

    private val listData = MutableLiveData<List<ShopItem>>()
    init {
        for(i in 0 until 10){
            val item = ShopItem("name$i", i, true)
            addShopItem(item)
        }
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return listData
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Element with id $shopItemId is not found")
    }

    override fun addShopItem(item: ShopItem) {
        if(item.id == ShopItem.UNDEFINED_ID){
            item.id = autoIncrementId++
        }
        shopList.add(item)
        updateLD()
    }

    override fun deleteShopItem(item: ShopItem) {
        shopList.remove(item)
        updateLD()
    }

    override fun editShopItem(item: ShopItem) {
        /*deleteShopItem(getShopItem(item.id))*/
        val old = getShopItem(item.id)
        shopList.remove(old)
        addShopItem(item)
    }

    private fun updateLD(){
        listData.value = shopList.toList()
    }
}