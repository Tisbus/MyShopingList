package com.example.myshopinglist.presentation

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

    fun getShopItem(item : ShopItem) : ShopItem{
        return getShopItemUseCase.getShopItem(item.id)
    }

    fun editShopItem(item : ShopItem){
        return editShopItemUseCase.editShopItem(item)
    }

    fun addShopItem(name : String, count : String) {
        val nameItem = inputName(name)
        val countItem = inputCount(count)
        if(validateInput(nameItem, countItem)){
            val result = ShopItem(nameItem, countItem, true)
            addShopItemUseCase.addShopItem(result)
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
            TODO("add show error")
            result = false
        }
        if(count <= 0){
            TODO("add show error")
            result = false
        }
        return result
    }

}