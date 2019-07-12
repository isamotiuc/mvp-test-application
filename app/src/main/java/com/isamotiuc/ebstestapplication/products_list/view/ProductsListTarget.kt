package com.isamotiuc.ebstestapplication.products_list.view

import androidx.lifecycle.LifecycleOwner
import com.isamotiuc.ebstestapplication.products_list.data.Product

interface ProductsListTarget : LifecycleOwner {
    fun addProducts(products: List<Product>)
    fun startOpenedProductActivity(id: Int)
    fun startFavoritesListActivity()
    fun updateProducts(products: List<Product>)
    fun showInternetErrorPopUp()
}