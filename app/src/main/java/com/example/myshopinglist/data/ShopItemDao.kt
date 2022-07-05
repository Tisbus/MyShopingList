package com.example.myshopinglist.data

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShopItemDao {
    @Query("SELECT * FROM shop_items")
    fun getShopItemList() : LiveData<List<ShopItemDBModel>>
    @Query("SELECT * FROM shop_items")
    fun getShopListCursor(): Cursor
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopItem(shopItemDBModel: ShopItemDBModel)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShopItemSync(shopItemDBModel: ShopItemDBModel)
    @Query("DELETE FROM shop_items WHERE id = :shopId")
    suspend fun deleteShopItem(shopId : Int)
    @Query("DELETE FROM shop_items WHERE id = :shopId")
    fun deleteShopItemSync(shopId : Int) : Int
    @Query("SELECT * FROM shop_items WHERE id = :shopId")
    suspend fun getShopItemId(shopId : Int) : ShopItemDBModel
}