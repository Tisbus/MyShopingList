package com.example.myshopinglist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myshopinglist.R
import com.example.myshopinglist.databinding.ShopItemFragmentBinding
import com.example.myshopinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment : Fragment() {

    private lateinit var viewShopModel: ShopViewModel
    private var screenMode: String = UNKNOWN_MODE
    private var shopId: Int = ShopItem.UNDEFINED_ID
    private lateinit var onEditingFinishedListener : OnEditingFinishedListener

    private var _bind : ShopItemFragmentBinding? = null
    private val bind : ShopItemFragmentBinding
    get() = _bind ?: throw java.lang.RuntimeException("ShopItemFragmentBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEditingFinishedListener){
            onEditingFinishedListener = context
        }else{
            throw java.lang.RuntimeException("Activity must implements OnEditingFinishedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = ShopItemFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewShopModel = ViewModelProvider(this)[ShopViewModel::class.java]
        bind.viewModel = viewShopModel
        bind.lifecycleOwner = viewLifecycleOwner
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
         viewShopModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }
    private fun addListenerChangeText() {
        bind.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewShopModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        bind.etCount.addTextChangedListener(object : TextWatcher {
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
        bind.saveButton.setOnClickListener {
            viewShopModel.editShopItem(
                bind.etName.text.toString(),
                bind.etCount.text.toString()
            )
        }
    }

    private fun addMode(){
        bind.saveButton.setOnClickListener {
            viewShopModel.addShopItem(
                bind.etName.text.toString(),
                bind.etCount.text.toString()
            )
        }
    }


    private fun parseParams(){
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param mode is not absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode: $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop id is absent")
            }
            shopId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    interface OnEditingFinishedListener{
        fun onEditingFinished()
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        const val UNKNOWN_MODE = ""

        fun newFragmentAddItem( ) : ShopItemFragment{
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newFragmentEditItem(shopId: Int) : ShopItemFragment{
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopId)
                }
            }
        }
    }

}