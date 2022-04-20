package com.example.myshopinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopinglist.data.ShopListRepositoryImpl
import com.example.myshopinglist.domain.DeleteShopItemUseCase
import com.example.myshopinglist.domain.EditShopItemUseCase
import com.example.myshopinglist.domain.GetShopListUseCase
import com.example.myshopinglist.domain.ShopItem
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(item : ShopItem){
        viewModelScope.launch {
            deleteShopItemUseCase.deleteShopItem(item)
        }
    }

    fun changeEnableState(item : ShopItem){
        viewModelScope.launch {
            val newItem = item.copy(enabled = !item.enabled)
            editShopItemUseCase.editShopItem(newItem)
        }
    }
}