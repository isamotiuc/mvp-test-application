package com.isamotiuc.ebstestapplication.products_list.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Product::class], version = 1)
abstract class ProductsDatabase : RoomDatabase() {
    abstract fun productsDAO(): ProductsDAO
}