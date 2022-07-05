package com.example.myshopinglist.presentation

import android.app.Application
import com.example.myshopinglist.di.DaggerApplicationComponent


class ShopApp : Application() {

    val component by lazy{
        DaggerApplicationComponent.factory()
            .create(this)
    }
}