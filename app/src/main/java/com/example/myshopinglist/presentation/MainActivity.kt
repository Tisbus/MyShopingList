package com.example.myshopinglist.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myshopinglist.R
import com.example.myshopinglist.presentation.adapter.ShopListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var adapterShop: ShopListAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecycler()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this, Observer {
            adapterShop.submitList(it)
            Log.d("check", it.toString())
        })
        addItem()
    }

    private fun addItem() {
        val floatAddItem = findViewById<FloatingActionButton>(R.id.floatAddItem)
        floatAddItem.setOnClickListener {
            val addItem = ShopItemActivity.newIntentAdd(this)
            startActivity(addItem)
        }
    }

    private fun setupRecycler() {
        val recyclerMain = setupRecyclerView()
        setupLongClickListener()
        setupShortClickListener()
        setupSwipeDeleteListener(recyclerMain)
    }

    private fun setupRecyclerView(): RecyclerView {
        val recyclerMain = findViewById<RecyclerView>(R.id.recyclerViewMain)
        with(recyclerMain) {
            adapterShop = ShopListAdapter()
            adapter = adapterShop
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        return recyclerMain
    }

    private fun setupSwipeDeleteListener(recyclerMain: RecyclerView) {
        val swipe = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        viewModel.deleteShopItem(adapterShop.currentList[viewHolder.adapterPosition])
                    }
                    ItemTouchHelper.RIGHT -> {
                        viewModel.deleteShopItem(adapterShop.currentList[viewHolder.adapterPosition])
                    }
                }
            }
        }

        val touchHelper = ItemTouchHelper(swipe)
        touchHelper.attachToRecyclerView(recyclerMain)
    }

    private fun setupShortClickListener() {
        adapterShop.onClickListener = {
            val editItem = ShopItemActivity.newIntentEdit(this, it.id)
            startActivity(editItem)
            Log.i("tap", "tap $it")
        }
    }

    private fun setupLongClickListener() {
        adapterShop.onShopItemLongClick = {

            viewModel.changeEnableState(it)
            Log.i("tap", "tap $it")
        }
    }

}