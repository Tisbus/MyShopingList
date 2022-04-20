package com.example.myshopinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.myshopinglist.domain.ShopItem
import com.example.myshopinglist.domain.ShopListRepository
import kotlinx.coroutines.CoroutineScope

class ShopListRepositoryImpl(application: Application) : ShopListRepository {

    private val mapper = ShopListMapper()

    private val db = AppDatabase.getInstance(application).shopItemDao()
/*    override fun getShopList(): LiveData<List<ShopItem>> = MediatorLiveData<List<ShopItem>>().apply {
        addSource(db.getShopItemList()){
            value = mapper.mapListDbModelToListEntity(it)
        }
    }*/

    override fun getShopList(): LiveData<List<ShopItem>> = Transformations.map(
        db.getShopItemList()
    ) {
        mapper.mapListDbModelToListEntity(it)
    }


    override suspend fun getShopItem(shopItemId: Int): ShopItem {
        return mapper.mapDbModelToEntity(db.getShopItemId(shopItemId))
    }

    override suspend fun addShopItem(item: ShopItem) {
        db.insertShopItem(mapper.mapEntityToDbModel(item))
    }

    override suspend fun deleteShopItem(item: ShopItem) {
        db.deleteShopItem(item.id)
    }

    override suspend fun editShopItem(item: ShopItem) {
        db.insertShopItem(mapper.mapEntityToDbModel(item))
    }
}