package com.isamotiuc.ebstestapplication.opened_product.view

import androidx.lifecycle.LifecycleOwner

interface OpenedProductTarget : LifecycleOwner {
    fun setPrice(discount: Int, price: Int)
    fun setImage(image: String)
    fun setTitle(title: String)
    fun setSubTitle(subTitle: String)
    fun setDescription(description: String)
    fun setFavorite(isChecked: Boolean)
    fun setActivityResult(result: Int)
}