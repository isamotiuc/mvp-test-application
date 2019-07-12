package com.isamotiuc.ebstestapplication.products_list.presentation

import android.app.Activity
import com.isamotiuc.ebstestapplication.base.BasePresenter
import com.isamotiuc.ebstestapplication.opened_product.view.OpenedProductActivity.Companion.REQUEST_CODE
import com.isamotiuc.ebstestapplication.products_list.data.Product
import com.isamotiuc.ebstestapplication.products_list.data.ProductsRepository
import com.isamotiuc.ebstestapplication.products_list.data.ProductsRepository.Companion.PAGE_LIMIT
import com.isamotiuc.ebstestapplication.products_list.view.ProductsListTarget
import javax.inject.Inject

class ProductsPresenter @Inject constructor(val productsRepository: ProductsRepository) :
    BasePresenter<ProductsListTarget>() {
    private var page = 0

    override fun onCreate() {
        loadProducts()
    }

    fun onListEndReached() {
        loadProducts()
    }

    private fun loadProducts() {
        addSubscription(
            productsRepository.getProducts(page * PAGE_LIMIT)
                .subscribe(
                    { products ->
                        if (products.isNotEmpty()) {
                            page++
                            target?.addProducts(products)
                        }
                    }, { target?.showInternetErrorPopUp() })
        )
    }

    fun onFavoriteClicked() {
        target?.startFavoritesListActivity()
    }

    fun onItemClicked(product: Product) {
        target?.startOpenedProductActivity(product.id)
    }

    fun onFavoriteItemChanged(product: Product, checked: Boolean) {
        addSubscription(
            productsRepository.updateFavorite(product.id, checked)
                .subscribe()
        )
    }

    fun onActivityResult(requestCode: Int, resultCode: Int) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            addSubscription(productsRepository.getDatabaseProducts(page * PAGE_LIMIT, 0)
                .subscribe { products ->
                    target?.updateProducts(products)
                })
        }
    }

    fun onRetryRequestClicked() {
        loadProducts()
    }
}