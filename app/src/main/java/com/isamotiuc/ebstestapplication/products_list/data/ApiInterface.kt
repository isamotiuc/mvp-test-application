package com.isamotiuc.ebstestapplication.products_list.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("products")
    fun getProducts(@Query("limit") limit: Int, @Query("offset") offset: Int): Single<List<Product>>

    @GET("product")
    fun getProduct(@Query("id") limit: Int): Single<Product>
}