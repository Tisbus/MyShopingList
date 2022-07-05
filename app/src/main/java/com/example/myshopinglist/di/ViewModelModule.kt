package com.example.myshopinglist.di

import androidx.lifecycle.ViewModel
import com.example.myshopinglist.presentation.MainViewModel
import com.example.myshopinglist.presentation.ShopViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ModuleKey(ShopViewModel::class)
    fun bindShopViewModel(viewModel: ShopViewModel) : ViewModel

    @Binds
    @IntoMap
    @ModuleKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel) : ViewModel
}