package com.example.myshopinglist.di

import android.app.Application
import com.example.myshopinglist.data.ShopContentProvider
import com.example.myshopinglist.presentation.MainActivity
import com.example.myshopinglist.presentation.ShopItemFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(app : MainActivity)
    fun inject(frag : ShopItemFragment)
    fun inject(provider : ShopContentProvider)

    @Component.Factory
    interface Factory{
    fun create(@BindsInstance application : Application) : ApplicationComponent
    }
}