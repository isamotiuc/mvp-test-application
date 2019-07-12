package com.isamotiuc.ebstestapplication.favorite_list.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.isamotiuc.ebstestapplication.R
import com.isamotiuc.ebstestapplication.base.BaseActivity
import com.isamotiuc.ebstestapplication.favorite_list.presentation.FavoritesPresenter
import com.isamotiuc.ebstestapplication.opened_product.view.OpenedProductActivity
import com.isamotiuc.ebstestapplication.opened_product.view.OpenedProductActivity.Companion.REQUEST_CODE
import com.isamotiuc.ebstestapplication.products_list.data.Product
import com.isamotiuc.ebstestapplication.products_list.view.ProductsAdapter
import kotlinx.android.synthetic.main.activity_favorites_list.*
import kotlinx.android.synthetic.main.activity_products_list.recycler_view
import kotlinx.android.synthetic.main.activity_products_list.toolbar

class FavoritesListActivity : BaseActivity<FavoritesPresenter, FavoritesListTarget>(),
    FavoritesListTarget {

    lateinit var productsAdapter: ProductsAdapter

    override fun getViewResourceId() = R.layout.activity_favorites_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar()

        setUpRecyclerView()
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setUpRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)

            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )

            productsAdapter = ProductsAdapter(
                { presenter.onItemClicked(it) },
                { product: Product, checked: Boolean ->
                    presenter.onFavoriteItemDeleted(product, checked)
                })
                .apply {
                    adapter = this
                }
        }
    }

    override fun startOpenedProductActivity(id: Int) {
        startActivityForResult(Intent(this, OpenedProductActivity::class.java).apply {
            putExtra(OpenedProductActivity.OPENED_PRODUCT_ID_KEY, id)
        }, REQUEST_CODE)
    }

    override fun addProducts(products: List<Product>) {
        productsAdapter.updateProducts(products)
    }

    override fun updateProducts(products: List<Product>) {
        productsAdapter.updateProducts(products)
    }

    override fun showEmptyView() {
        empty_text_view.visibility = View.VISIBLE
    }

    override fun hideEmptyView() {
        empty_text_view.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.onActivityResult(requestCode, resultCode)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.checked_favorite_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_favorite) {
            presenter.onFavoriteIconClicked()
        }
        return true
    }

    override fun setActivityResult(result: Int) {
        setResult(result)
    }
}
