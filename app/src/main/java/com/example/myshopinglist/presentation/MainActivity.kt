package com.example.myshopinglist.presentation

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myshopinglist.R
import com.example.myshopinglist.databinding.ActivityMainBinding
import com.example.myshopinglist.domain.ShopItem
import com.example.myshopinglist.presentation.adapter.ShopListAdapter
import javax.inject.Inject
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var adapterShop: ShopListAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var bind: ActivityMainBinding

    @Inject
    lateinit var shopViewModelFactory: ShopViewModelFactory

    private val component by lazy {
        (application as ShopApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setupRecycler()
        viewModel = ViewModelProvider(this, shopViewModelFactory)[MainViewModel::class.java]
        viewModel.shopList.observe(this, Observer {
            adapterShop.submitList(it)
        })
        addItem()
        thread {
            val cursor = contentResolver.query(
                Uri.parse("content://com.example.myshopinglist/shop_items"),
                null,
                null,
                null,
                null,
                null
            )
            while (cursor?.moveToNext() == true) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val count = cursor.getInt(cursor.getColumnIndexOrThrow("count"))
                val enabled = cursor.getInt(cursor.getColumnIndexOrThrow("enabled")) > 0
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enabled = enabled
                )
                Log.d("shop_items", shopItem.toString())
            }
            cursor?.close()
        }

    }

    private fun isOnePaneMode(): Boolean {
        return bind.shopItemContainer == null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun addItem() {
        bind.floatAddItem.setOnClickListener {
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
        val recyclerMain = bind.recyclerViewMain
        with(bind.recyclerViewMain) {
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
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        viewModel.deleteShopItem(adapterShop.currentList[viewHolder.adapterPosition])
/*                        thread {
                            contentResolver.delete(
                                Uri.parse("content://com.example.myshopinglist/shop_items"),
                                null,
                                arrayOf(adapterShop.currentList[viewHolder.adapterPosition].id.toString())
                            )
                        }*/
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