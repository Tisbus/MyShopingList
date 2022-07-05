package com.example.myshopinglist.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.myshopinglist.domain.ShopItem
import com.example.myshopinglist.presentation.ShopApp
import javax.inject.Inject

class ShopContentProvider : ContentProvider() {

    @Inject
    lateinit var shopListDao: ShopItemDao

    @Inject
    lateinit var mapper: ShopListMapper

    private val component by lazy {
        (context as ShopApp).component
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("com.example.myshopinglist", "shop_items", URI_MATCHER_CODE)
        addURI("com.example.myshopinglist", "shop_items/#", URI_MATCHER_ID_CODE)
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            URI_MATCHER_CODE -> {
                shopListDao.getShopListCursor()
            }
            else -> {
                null
            }
        }
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when (uriMatcher.match(uri)) {
            URI_MATCHER_CODE -> {
                if (values == null) return null
                val id = values.getAsInteger("id")
                val name = values.getAsString("name")
                val count = values.getAsInteger("count")
                val enabled = values.getAsBoolean("enabled")
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enabled = enabled
                )
                shopListDao.insertShopItemSync(mapper.mapEntityToDbModel(shopItem))
            }
        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        when (uriMatcher.match(uri)) {
            URI_MATCHER_CODE -> {
                val id = selectionArgs?.get(0)?.toInt() ?: -1
                return shopListDao.deleteShopItemSync(id)
            }
        }
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int {
        TODO("Not yet implemented")
    }

    companion object {
        const val URI_MATCHER_CODE = 100
        const val URI_MATCHER_ID_CODE = 101
    }
}