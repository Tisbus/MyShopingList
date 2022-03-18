package com.example.myshopinglist.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.myshopinglist.R
import com.example.myshopinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText

class ShopItemActivity : AppCompatActivity() {

    private lateinit var itemModel : ShopViewModel
    private lateinit var viewModel : MainViewModel
    private lateinit var saveButton:Button
    private lateinit var etName:TextInputEditText
    private lateinit var etCount:TextInputEditText
    private var num:Int = 0
    private var itemId:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        if(intent.hasExtra("word")){
            num = intent.getIntExtra("word", 0)
        }
        if(intent.hasExtra("item")){
            itemId = intent.getIntExtra("item", 0)
        }
        itemModel = ViewModelProvider(this)[ShopViewModel::class.java]
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val itemShop = viewModel.shopList.value?.get(itemId)
        saveButton = findViewById(R.id.saveButton)
        etName = findViewById(R.id.etName)
        etCount = findViewById(R.id.etCount)
        if(num == 666){
            if (itemShop != null) {
                etName.setText(itemShop.name)
                etCount.setText(itemShop.count.toString())
            }
        }
        if(num == 666){
            Toast.makeText(this, "id : $itemId and this is status Edit" , Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "id : $itemId and this is status Save" , Toast.LENGTH_SHORT).show()
        }
        saveButton.setOnClickListener {
            if(num == 666){
                if(itemShop != null){
                    val newItem = ShopItem(etName.text.toString(), etCount.text.toString().toInt(), true, itemShop.id)
                    itemModel.editShopItem(newItem)
                }
            }else{
                itemModel.addShopItem(etName.text.toString(), etCount.text.toString())
            }
            goMain()
        }

    }
    private fun goMain(){
        val goMain = Intent(this, MainActivity::class.java)
        startActivity(goMain)
    }
}