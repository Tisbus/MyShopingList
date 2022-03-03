package com.example.myshopinglist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myshopinglist.data.ShopListRepositoryImpl
import com.example.myshopinglist.domain.DeleteShopItemUseCase
import com.example.myshopinglist.domain.EditShopItemUseCase
import com.example.myshopinglist.domain.GetShopListUseCase
import com.example.myshopinglist.domain.ShopItem

class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = MutableLiveData<List<ShopItem>>()

    fun getShopList(){
        val list = getShopListUseCase.getShopList()
        shopList.value = list
    }

    fun deleteShopItem(item : ShopItem){
        deleteShopItemUseCase.deleteShopItem(item)
        getShopList()
    }

    fun changeEnableState(item : ShopItem){
        val newItem = item.copy(enabled = !item.enabled)
        editShopItemUseCase.editShopItem(newItem)
        getShopList()
    }


}