package com.isamotiuc.ebstestapplication.opened_product.view

import android.os.Bundle
import com.bumptech.glide.Glide
import com.isamotiuc.ebstestapplication.R
import com.isamotiuc.ebstestapplication.base.BaseActivity
import com.isamotiuc.ebstestapplication.opened_product.presentation.OpenedProductPresenter
import kotlinx.android.synthetic.main.activity_opened_product.*
import kotlinx.android.synthetic.main.product_list_item.*


class OpenedProductActivity : BaseActivity<OpenedProductPresenter, OpenedProductTarget>(),
    OpenedProductTarget {

    companion object {
        const val OPENED_PRODUCT_ID_KEY = "product_id"
        const val REQUEST_CODE = 1
    }

    override fun getViewResourceId() = R.layout.activity_opened_product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar()

        presenter.onCreate(intent.getIntExtra(OPENED_PRODUCT_ID_KEY, 0))

        favorite_toggle_button.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed) {
                presenter.onFavoriteItemChanged(isChecked)
            }
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun setPrice(discount: Int, price: Int) {
        price_discount_view.setPrice(discount, price)
    }

    override fun setImage(image: String) {
        Glide.with(this)
            .load(image)
            .placeholder(R.drawable.ic_placeholder_128dp)
            .into(product_image_view)
    }

    override fun setTitle(title: String) {
        title_text_view.text = title
    }

    override fun setSubTitle(subTitle: String) {
        sub_title_text_view.text = subTitle
    }

    override fun setDescription(description: String) {
        information_text_view.text = description
    }

    override fun setFavorite(isChecked: Boolean) {
        favorite_toggle_button.isChecked = isChecked
    }

    override fun setActivityResult(result: Int) {
        setResult(result)
    }
}
