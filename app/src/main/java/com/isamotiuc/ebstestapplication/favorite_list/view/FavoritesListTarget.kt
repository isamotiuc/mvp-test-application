package com.isamotiuc.ebstestapplication.favorite_list.view

import androidx.lifecycle.LifecycleOwner
import com.isamotiuc.ebstestapplication.products_list.data.Product

interface FavoritesListTarget : LifecycleOwner {
    fun addProducts(products: List<Product>)
    fun startOpenedProductActivity(id: Int)
    fun updateProducts(products: List<Product>)
    fun finish()
    fun showEmptyView()
    fun hideEmptyView()
    fun setActivityResult(result: Int)
}