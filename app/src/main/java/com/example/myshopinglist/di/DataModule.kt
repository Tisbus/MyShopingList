package com.example.myshopinglist.di

import android.app.Application
import com.example.myshopinglist.data.AppDatabase
import com.example.myshopinglist.data.ShopItemDao
import com.example.myshopinglist.data.ShopListRepositoryImpl
import com.example.myshopinglist.domain.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    @Binds
    @ApplicationScope
    fun bindShopRepository(impl : ShopListRepositoryImpl) : ShopListRepository

    companion object{
        @Provides
        @ApplicationScope
        fun provideShopItemDao(application: Application) : ShopItemDao{
            return AppDatabase.getInstance(application).shopItemDao()
        }
    }
}