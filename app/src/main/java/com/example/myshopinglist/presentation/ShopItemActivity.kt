package com.example.myshopinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myshopinglist.R
import com.example.myshopinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity() {

    private lateinit var viewShopModel: ShopViewModel
    private var screenMode: String = UNKNOWN_MODE
    private var shopId: Int = ShopItem.UNDEFINED_ID
    private lateinit var nameET: EditText
    private lateinit var countET: EditText
    private lateinit var saveButton: Button
    private lateinit var tilCount: TextInputLayout
    private lateinit var tilName: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        viewShopModel = ViewModelProvider(this)[ShopViewModel::class.java]
        initView()
        addListenerChangeText()
        launchScreenMode()
        observeError()
    }

    private fun launchScreenMode() {
        when (screenMode) {
            MODE_EDIT -> editMode()
            MODE_ADD -> addMode()
        }
    }

    private fun observeError(){
        viewShopModel.errorInputName.observe(this) {
            val message = if (it) {
                getString(R.string.empty_string)
            } else {
                null
            }
            tilName.error = message
        }
        viewShopModel.errorInputCount.observe(this) {
            val message = if (it) {
                getString(R.string.error)
            } else {
                null
            }
            tilCount.error = message
        }
        viewShopModel.shouldCloseScreen.observe(this) {
            finish()
        }
    }
    private fun addListenerChangeText() {
        nameET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewShopModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        countET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewShopModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun editMode() {
        viewShopModel.getShopItem(shopId)
        viewShopModel.getShopItemLD.observe(this) {
            nameET.setText(it.name)
            countET.setText(it.count.toString())
        }
        saveButton.setOnClickListener {
            viewShopModel.editShopItem(
                nameET.text.toString(),
                countET.text.toString()
            )
        }
    }

    private fun addMode(){
        saveButton.setOnClickListener {
            viewShopModel.addShopItem(
                nameET.text.toString(),
                countET.text.toString()
            )
        }
    }


    private fun parseIntent(){
        if(!intent.hasExtra(EXTRA_SCREEN_MODE)){
            throw RuntimeException("Param mode is not absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if(mode != MODE_EDIT && mode != MODE_ADD){
            throw RuntimeException("Unknown screen mode: $mode")
        }
        screenMode = mode
        if(screenMode == MODE_EDIT){
            if(!intent.hasExtra(EXTRA_SHOP_ITEM_ID)){
                throw RuntimeException("Param shop id is absent")
            }
            shopId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun initView() {
        nameET = findViewById(R.id.etName)
        countET = findViewById(R.id.etCount)
        saveButton = findViewById(R.id.saveButton)
        tilCount = findViewById(R.id.tilCount)
        tilName = findViewById(R.id.tilName)
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
        private const val UNKNOWN_MODE = ""


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