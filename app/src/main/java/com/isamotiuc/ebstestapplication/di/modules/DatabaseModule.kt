package com.isamotiuc.ebstestapplication.di.modules

import android.app.Application
import androidx.room.Room
import com.isamotiuc.ebstestapplication.products_list.data.ProductsDAO
import com.isamotiuc.ebstestapplication.products_list.data.ProductsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideProductsDatabase(app: Application): ProductsDatabase = Room.databaseBuilder(
        app,
        ProductsDatabase::class.java, "products_db"
    ).build()

    @Provides
    @Singleton
    fun provideCryptocurrenciesDao(
        database: ProductsDatabase
    ): ProductsDAO = database.productsDAO()
}