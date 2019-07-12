package com.isamotiuc.ebstestapplication.opened_product.data

import com.isamotiuc.ebstestapplication.products_list.data.ApiInterface
import com.isamotiuc.ebstestapplication.products_list.data.Product
import com.isamotiuc.ebstestapplication.products_list.data.ProductsDAO
import com.isamotiuc.ebstestapplication.utils.ApiSchedulerTransformer
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val productsDAO: ProductsDAO
) {

    fun getProduct(id: Int): Single<Product> {
        return getApiProduct(id).onErrorResumeNext {
            getDatabaseProduct(id)
        }.compose(ApiSchedulerTransformer())
    }

    private fun getApiProduct(id: Int): Single<Product> {
        return Single.zip(apiInterface.getProduct(id), productsDAO.getFavorite(id),
            BiFunction { apiProduct, dbProduct ->
                apiProduct.favorite = dbProduct.favorite
                productsDAO.insertProduct(apiProduct)
                apiProduct
            })
    }

    private fun getDatabaseProduct(id: Int): Single<Product> {
        return productsDAO.getProduct(id)
    }

    fun updateFavorite(productId: Int, enabled: Boolean): Single<Int> {
        return productsDAO.updateFavorite(enabled, productId)
            .compose(ApiSchedulerTransformer())

    }
}