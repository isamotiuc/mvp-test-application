package com.isamotiuc.ebstestapplication.products_list.data

import com.isamotiuc.ebstestapplication.utils.ApiSchedulerTransformer
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val productsDAO: ProductsDAO
) {
    companion object {
        const val PAGE_LIMIT = 10
    }

    fun getProducts(offset: Int): Single<List<Product>> {
        return getApiProducts(offset)
            .onErrorResumeNext {
                productsDAO.getProducts(PAGE_LIMIT, offset).map {
                    if (it.isEmpty()) {
                        throw NoInternetException()
                    }
                    it
                }
            }.compose(ApiSchedulerTransformer())
    }

    fun updateFavorite(productId: Int, enabled: Boolean): Single<Int> {
        return productsDAO.updateFavorite(enabled, productId)
            .compose(ApiSchedulerTransformer())
    }

    private fun getApiProducts(offset: Int): Single<List<Product>> {
        return Single.zip(apiInterface.getProducts(PAGE_LIMIT, offset),
            productsDAO.getFavorites(),
            BiFunction { apiProducts, favorites ->
                favorites.forEach { favorite ->
                    apiProducts.forEach { apiProduct ->
                        if (favorite.id == apiProduct.id) {
                            apiProduct.favorite = true
                        }
                    }
                }
                productsDAO.insertAllProducts(apiProducts)
                apiProducts
            })
    }

    fun getDatabaseProducts(pageLimit: Int, offset: Int): Single<List<Product>> {
        return productsDAO.getProducts(pageLimit, offset)
            .compose(ApiSchedulerTransformer())
    }
}

class NoInternetException : Throwable()
