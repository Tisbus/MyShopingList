package com.example.myshopinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myshopinglist.domain.ShopItem
import com.example.myshopinglist.domain.ShopListRepository
import java.lang.RuntimeException
import kotlin.random.Random

object ShopListRepositoryImpl : ShopListRepository {

    private val shopList = sortedSetOf<ShopItem>({ o1, o2 -> o1.id.compareTo(o2.id) })

    private var autoIncrementId = 0

    private val listData = MutableLiveData<List<ShopItem>>()
    init {
        for(i in 0 until 100){
            val item = ShopItem("name$i", i, Random.nextBoolean())
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