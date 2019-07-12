package com.isamotiuc.ebstestapplication.di.modules

import com.isamotiuc.ebstestapplication.favorite_list.view.FavoritesListActivity
import com.isamotiuc.ebstestapplication.opened_product.view.OpenedProductActivity
import com.isamotiuc.ebstestapplication.products_list.view.ProductsListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeProductsActivity(): ProductsListActivity

    @ContributesAndroidInjector
    abstract fun contributeProductActivity(): OpenedProductActivity

    @ContributesAndroidInjector
    abstract fun contributeFavoriteListActivity(): FavoritesListActivity
}