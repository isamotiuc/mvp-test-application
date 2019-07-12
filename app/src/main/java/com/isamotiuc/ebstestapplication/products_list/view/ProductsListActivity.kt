package com.isamotiuc.ebstestapplication.products_list.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.isamotiuc.ebstestapplication.R
import com.isamotiuc.ebstestapplication.base.BaseActivity
import com.isamotiuc.ebstestapplication.favorite_list.view.FavoritesListActivity
import com.isamotiuc.ebstestapplication.opened_product.view.OpenedProductActivity
import com.isamotiuc.ebstestapplication.opened_product.view.OpenedProductActivity.Companion.OPENED_PRODUCT_ID_KEY
import com.isamotiuc.ebstestapplication.opened_product.view.OpenedProductActivity.Companion.REQUEST_CODE
import com.isamotiuc.ebstestapplication.products_list.data.Product
import com.isamotiuc.ebstestapplication.products_list.presentation.ProductsPresenter
import com.isamotiuc.ebstestapplication.utils.InfiniteScrollListener
import kotlinx.android.synthetic.main.activity_products_list.*


class ProductsListActivity : BaseActivity<ProductsPresenter, ProductsListTarget>(),
    ProductsListTarget {

    lateinit var productsAdapter: ProductsAdapter

    override fun getViewResourceId(): Int = R.layout.activity_products_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        setUpRecyclerView()
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

            addOnScrollListener(
                InfiniteScrollListener(
                    { presenter.onListEndReached() },
                    layoutManager as LinearLayoutManager
                )
            )

            productsAdapter = ProductsAdapter(
                { presenter.onItemClicked(it) },
                { product: Product, checked: Boolean ->
                    presenter.onFavoriteItemChanged(product, checked)
                })
                .apply {
                    adapter = this
                }
        }
    }

    override fun updateProducts(products: List<Product>) {
        productsAdapter.updateProducts(products)
    }

    override fun addProducts(products: List<Product>) {
        productsAdapter.addProducts(products)
    }

    override fun startOpenedProductActivity(id: Int) {
        startActivityForResult(Intent(this, OpenedProductActivity::class.java).apply {
            putExtra(OPENED_PRODUCT_ID_KEY, id)
        }, REQUEST_CODE)
    }

    override fun startFavoritesListActivity() {
        startActivityForResult(Intent(this, FavoritesListActivity::class.java), REQUEST_CODE)
    }

    override fun showInternetErrorPopUp() {
        Snackbar.make(view_parent, R.string.no_internet_connection, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.retry) { presenter.onRetryRequestClicked() }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.onActivityResult(requestCode, resultCode)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.unchecked_favorite_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_favorite) {
            presenter.onFavoriteClicked()
        }
        return true
    }
}
