package com.example.myshopinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myshopinglist.data.ShopListRepositoryImpl
import com.example.myshopinglist.domain.AddShopItemUseCase
import com.example.myshopinglist.domain.EditShopItemUseCase
import com.example.myshopinglist.domain.GetShopItemUseCase
import com.example.myshopinglist.domain.ShopItem

class ShopViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName : LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount : LiveData<Boolean>
        get() = _errorInputCount

    private val _getShopItem = MutableLiveData<ShopItem>()
    val getShopItemLD : LiveData<ShopItem>
        get() = _getShopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen : LiveData<Unit>
        get() = _shouldCloseScreen

    fun getShopItem(itemId : Int){
        val itemShop = getShopItemUseCase.getShopItem(itemId)
        _getShopItem.value = itemShop
    }

    fun editShopItem(name : String, count : String){
        val nameItem = inputName(name)
        val countItem = inputCount(count)
        if(validateInput(nameItem, countItem)){
            _getShopItem.value?.let {
                val item = it.copy(name = nameItem, count = countItem)
                editShopItemUseCase.editShopItem(item)
                finishWork()
            }

        }
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    fun addShopItem(name : String, count : String) {
        val nameItem = inputName(name)
        val countItem = inputCount(count)
        if(validateInput(nameItem, countItem)){
            val result = ShopItem(nameItem, countItem, true)
            addShopItemUseCase.addShopItem(result)
            finishWork()
        }
    }

    private fun inputName(inputName : String?) : String{
        return inputName?.trim() ?: ""
    }
    private fun inputCount(inputCount : String?) : Int{
        return try{
            inputCount?.trim()?.toInt() ?: 0
        }catch (
            e : Exception
        ){
            0
        }
    }
    private fun validateInput(name : String, count : Int) : Boolean{
        var result = true
        if(name.isBlank()){
            _errorInputName.value = true
            result = false
        }else{
            _errorInputName.value = false
        }
        if(count <= 0){
            _errorInputCount.value = true
            result = false
        }else{
            _errorInputCount.value = false
        }
        return result
    }

}