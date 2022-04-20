package com.example.myshopinglist.data

import com.example.myshopinglist.domain.ShopItem

class ShopListMapper {

    fun mapEntityToDbModel(shopItem: ShopItem) = ShopItemDBModel(
        id = shopItem.id,
        name = shopItem.name,
        count = shopItem.count,
        enabled = shopItem.enabled
    )

    fun mapDbModelToEntity(shopItem: ShopItemDBModel) = ShopItem(
        id = shopItem.id,
        name = shopItem.name,
        count = shopItem.count,
        enabled = shopItem.enabled
    )

    fun mapListDbModelToListEntity(list : List<ShopItemDBModel>) = list.map {
        mapDbModelToEntity(it)
    }

    fun mapListEntityToDbListModel(list : List<ShopItem>) = list.map {
        mapEntityToDbModel(it)
    }
}