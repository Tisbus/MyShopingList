package com.example.myshopinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myshopinglist.data.ShopListRepositoryImpl
import com.example.myshopinglist.domain.AddShopItemUseCase
import com.example.myshopinglist.domain.EditShopItemUseCase
import com.example.myshopinglist.domain.GetShopItemUseCase
import com.example.myshopinglist.domain.ShopItem
import kotlinx.coroutines.launch

class ShopViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _getShopItem = MutableLiveData<ShopItem>()
    val getShopItemLD: LiveData<ShopItem>
        get() = _getShopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getShopItem(itemId: Int) {
        viewModelScope.launch {
            val itemShop = getShopItemUseCase.getShopItem(itemId)
            _getShopItem.value = itemShop
        }
    }

    fun editShopItem(name: String, count: String) {
        val nameItem = inputName(name)
        val countItem = inputCount(count)
        if (validateInput(nameItem, countItem)) {

            _getShopItem.value?.let {
                viewModelScope.launch {
                    val item = it.copy(name = nameItem, count = countItem)
                    editShopItemUseCase.editShopItem(item)
                    finishWork()
                }
            }

        }
    }

    fun addShopItem(name: String, count: String) {
        val nameItem = inputName(name)
        val countItem = inputCount(count)
        if (validateInput(nameItem, countItem)) {
            viewModelScope.launch {
                val result = ShopItem(nameItem, countItem, true)
                addShopItemUseCase.addShopItem(result)
                finishWork()
            }

        }
    }

    private fun inputName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun inputCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (
            e: Exception
        ) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        } else {
            _errorInputName.value = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        } else {
            _errorInputCount.value = false
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }
}