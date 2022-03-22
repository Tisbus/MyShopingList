package com.example.myshopinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myshopinglist.R

class ShopItemActivity : AppCompatActivity() {

    private lateinit var viewShopModel: ShopViewModel
    private lateinit var mode: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        viewShopModel = ViewModelProvider(this)[ShopViewModel::class.java]
        if (intent.hasExtra(EXTRA_SCREEN_MODE)) {
            mode = intent.getStringExtra(EXTRA_SCREEN_MODE).toString()
        }
        val nameET = findViewById<EditText>(R.id.etName)
        val countET = findViewById<EditText>(R.id.etCount)

        val saveButton = findViewById<Button>(R.id.saveButton)
        if (mode == MODE_EDIT) {
            val itemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, 0)
            viewShopModel.getShopItem(itemId)
            nameET.setText(viewShopModel.getShopItemLD.value?.name ?: "")
            countET.setText(viewShopModel.getShopItemLD.value?.count.toString())
        }
        saveButton.setOnClickListener {
            if (mode == MODE_EDIT) {
                viewShopModel.editShopItem(nameET.text.toString(), countET.text.toString())
                goMain()
            } else {
                viewShopModel.addShopItem(nameET.text.toString(), countET.text.toString())
                goMain()
            }
        }

    }

    private fun goMain() {
        val goMain = Intent(this, MainActivity::class.java)
        startActivity(goMain)
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"

        fun newIntentAdd(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEdit(context: Context, itemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, itemId)
            return intent
        }
    }
}