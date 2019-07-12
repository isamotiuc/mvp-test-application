package com.isamotiuc.ebstestapplication.favorite_list.presentation

import android.app.Activity.RESULT_OK
import com.isamotiuc.ebstestapplication.base.BasePresenter
import com.isamotiuc.ebstestapplication.favorite_list.data.FavoritesRepository
import com.isamotiuc.ebstestapplication.favorite_list.view.FavoritesListTarget
import com.isamotiuc.ebstestapplication.opened_product.view.OpenedProductActivity
import com.isamotiuc.ebstestapplication.products_list.data.Product
import javax.inject.Inject

class FavoritesPresenter @Inject constructor(val productsRepository: FavoritesRepository) :
    BasePresenter<FavoritesListTarget>() {

    override fun onCreate() {
        loadProducts { target?.addProducts(it) }
    }

    private fun loadProducts(addProducts: (List<Product>) -> Unit) {
        addSubscription(
            productsRepository.getFavorites()
                .subscribe { favorites ->
                    checkEmptyView(favorites)
                    addProducts(favorites)
                }
        )
    }


    fun onFavoriteItemDeleted(product: Product, checked: Boolean) {
        addSubscription(productsRepository.updateFavorite(product.id, checked)
            .flatMap { productsRepository.getFavorites() }
            .subscribe { favorites ->
                target?.setActivityResult(RESULT_OK)
                checkEmptyView(favorites)
                target?.updateProducts(favorites)
            })

    }

    fun onItemClicked(product: Product) {
        target?.startOpenedProductActivity(product.id)
    }


    fun onActivityResult(requestCode: Int, resultCode: Int) {
        if (requestCode == OpenedProductActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            target?.setActivityResult(RESULT_OK)
            loadProducts { target?.updateProducts(it) }
        }
    }

    fun onFavoriteIconClicked() {
        target?.finish()
    }

    private fun checkEmptyView(favorites: List<Product>) {
        if (favorites.isEmpty()) {
            target?.showEmptyView()
        } else {
            target?.hideEmptyView()
        }
    }
}