package com.isamotiuc.ebstestapplication.products_list.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface ProductsDAO {

    @Query("SELECT * FROM products limit :limit offset :offset")
    fun getProducts(limit: Int, offset: Int): Single<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id ")
    fun getProduct(id: Int): Single<Product>

    @Query("SELECT * FROM products WHERE favorite = 1")
    fun getFavorites(): Single<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getFavorite(id: Int): Single<Product>

    @Query("UPDATE products SET favorite = :enabled WHERE id = :productId")
    fun updateFavorite(enabled: Boolean, productId: Int): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllProducts(products: List<Product>)
}