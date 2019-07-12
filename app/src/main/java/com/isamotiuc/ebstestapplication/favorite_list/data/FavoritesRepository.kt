package com.isamotiuc.ebstestapplication.favorite_list.data

import com.isamotiuc.ebstestapplication.products_list.data.Product
import com.isamotiuc.ebstestapplication.products_list.data.ProductsDAO
import com.isamotiuc.ebstestapplication.utils.ApiSchedulerTransformer
import io.reactivex.Single
import javax.inject.Inject

class FavoritesRepository @Inject constructor(private val productsDAO: ProductsDAO) {
    fun getFavorites(): Single<List<Product>> {
        return productsDAO.getFavorites()
            .compose(ApiSchedulerTransformer())

    }

    fun updateFavorite(productId: Int, enabled: Boolean): Single<Int> {
        return productsDAO.updateFavorite(enabled, productId)
            .compose(ApiSchedulerTransformer())
    }
}