package com.example.myshopinglist.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myshopinglist.R
import com.example.myshopinglist.presentation.adapter.ShopListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var adapterShop: ShopListAdapter
    private lateinit var viewModel: MainViewModel
    private var shopItemContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shopItemContainer = findViewById(R.id.shop_item_container)
        setupRecycler()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this, Observer {
            adapterShop.submitList(it)
        })
        addItem()
    }

    private fun isOnePaneMode(): Boolean {
        return shopItemContainer == null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun addItem() {
        val floatAddItem = findViewById<FloatingActionButton>(R.id.floatAddItem)
        floatAddItem.setOnClickListener {
            if (isOnePaneMode()) {
                val addItem = ShopItemActivity.newIntentAdd(this)
                startActivity(addItem)
            } else {
                launchFragment(ShopItemFragment.newFragmentAddItem())
            }
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
            if (isOnePaneMode()) {
                val editItem = ShopItemActivity.newIntentEdit(this, it.id)
                startActivity(editItem)
            } else {
                launchFragment(ShopItemFragment.newFragmentEditItem(it.id))
            }
        }
    }

    private fun setupLongClickListener() {
        adapterShop.onShopItemLongClick = {
            viewModel.changeEnableState(it)
        }
    }

    override fun onEditingFinished() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

}