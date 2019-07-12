package com.isamotiuc.ebstestapplication.opened_product.presentation

import android.app.Activity
import com.isamotiuc.ebstestapplication.base.BasePresenter
import com.isamotiuc.ebstestapplication.opened_product.data.ProductRepository
import com.isamotiuc.ebstestapplication.opened_product.view.OpenedProductTarget
import com.isamotiuc.ebstestapplication.products_list.data.Product
import javax.inject.Inject

class OpenedProductPresenter @Inject constructor(val productRepository: ProductRepository) :
    BasePresenter<OpenedProductTarget>() {

    lateinit var product: Product

    fun onCreate(productId: Int) {
        addSubscription(
            productRepository.getProduct(productId)
                .subscribe(
                    { product ->
                        initProduct(product)
                    }, {})
        )
    }

    private fun initProduct(product: Product) {
        this.product = product
        target?.apply {
            setImage(product.image)
            setTitle(product.title)
            setSubTitle(product.shortDescription)
            setPrice(product.salePercent, product.price)
            setDescription(product.details)
            setFavorite(product.favorite)
        }
    }

    fun onFavoriteItemChanged(checked: Boolean) {
        addSubscription(productRepository.updateFavorite(product.id, checked)
            .subscribe { _ -> target?.setActivityResult(Activity.RESULT_OK) })

    }
}