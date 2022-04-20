package com.example.myshopinglist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShopItemDao {
    @Query("SELECT * FROM shop_item")
    fun getShopItemList() : LiveData<List<ShopItemDBModel>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopItem(shopItemDBModel: ShopItemDBModel)
    @Query("DELETE FROM shop_item WHERE id = :shopId")
    suspend fun deleteShopItem(shopId : Int)
    @Query("SELECT * FROM shop_item WHERE id = :shopId")
    suspend fun getShopItemId(shopId : Int) : ShopItemDBModel
}