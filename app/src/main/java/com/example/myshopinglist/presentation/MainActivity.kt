package com.example.myshopinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshopinglist.R
import com.example.myshopinglist.domain.ShopListRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}