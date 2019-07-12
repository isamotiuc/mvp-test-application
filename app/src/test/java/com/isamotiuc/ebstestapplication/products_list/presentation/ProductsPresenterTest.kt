package com.isamotiuc.ebstestapplication.products_list.presentation

import android.app.Activity
import com.isamotiuc.ebstestapplication.PresenterTestCase
import com.isamotiuc.ebstestapplication.opened_product.view.OpenedProductActivity
import com.isamotiuc.ebstestapplication.products_list.data.NoInternetException
import com.isamotiuc.ebstestapplication.products_list.data.Product
import com.isamotiuc.ebstestapplication.products_list.data.ProductsRepository
import com.isamotiuc.ebstestapplication.products_list.view.ProductsListTarget
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock

class ProductsPresenterTest : PresenterTestCase<ProductsPresenter, ProductsListTarget>() {
    override lateinit var targetMock: ProductsListTarget
    override val targetClazz: Class<ProductsListTarget> = ProductsListTarget::class.java

    @InjectMocks
    override lateinit var presenter: ProductsPresenter

    @Mock
    lateinit var productRepository: ProductsRepository

    private val response: List<Product> = listOf(
        Product(
            0, "test title", "test description",
            "image url", 100, 5, "test details", false
        ),
        Product(
            1, "test title 1", "test description 1",
            "image url 1", 1000, 0, "test details 1", false
        )
    )

    @Test
    fun `sets products on create`() {
        setsProducts { presenter.onCreate() }
    }

    @Test
    fun `loads products on create`() {
        loadsProducts { presenter.onCreate() }
    }

    @Test
    fun `shows error popup on failed response`() {
        showsErrorPopUp { presenter.onCreate() }
    }

    @Test
    fun `sets products on list reached end`() {
        setsProducts { presenter.onListEndReached() }
    }

    @Test
    fun `loads products on list reached end`() {
        loadsProducts { presenter.onListEndReached() }
    }

    @Test
    fun `shows error popup on list reached end`() {
        showsErrorPopUp { presenter.onListEndReached() }
    }

    @Test
    fun `sets products on retry click`() {
        setsProducts { presenter.onRetryRequestClicked() }
    }

    @Test
    fun `loads products on retry click`() {
        loadsProducts { presenter.onRetryRequestClicked() }
    }

    @Test
    fun `shows error popup on retry click`() {
        showsErrorPopUp { presenter.onRetryRequestClicked() }
    }

    @Test
    fun `changes page on nex request`() {
        whenever(productRepository.getProducts(any()))
            .thenReturn(Single.just(response))

        presenter.onCreate()
        presenter.onListEndReached()
        presenter.onListEndReached()

        inOrder(productRepository).run {
            verify(productRepository).getProducts(0)
            verify(productRepository).getProducts(ProductsRepository.PAGE_LIMIT)
            verify(productRepository).getProducts(2 * ProductsRepository.PAGE_LIMIT)
        }
    }

    @Test
    fun `starts activity on favorite clicked`() {
        presenter.onFavoriteClicked()

        verify(targetMock).startFavoritesListActivity()
    }

    @Test
    fun `starts activity on product clicked`() {
        presenter.onItemClicked(response[0])

        verify(targetMock).startOpenedProductActivity(response[0].id)
    }

    @Test
    fun `saves new favorite on favorite button clicked`() {
        whenever(productRepository.updateFavorite(any(), any())).thenReturn(Single.just(1))

        presenter.onFavoriteItemChanged(response[0], true)

        verify(productRepository).updateFavorite(response[0].id, true)
    }

    @Test
    fun `gets data from database on favorites activity result`() {
        whenever(productRepository.getDatabaseProducts(any(), any()))
            .thenReturn(
                Single.just(response)
            )

        presenter.onActivityResult(OpenedProductActivity.REQUEST_CODE, Activity.RESULT_OK)

        verify(productRepository).getDatabaseProducts(0, 0)
    }

    @Test
    fun `updates products on favorites activity result`() {
        whenever(productRepository.getDatabaseProducts(any(), any()))
            .thenReturn(Single.just(response))

        presenter.onActivityResult(OpenedProductActivity.REQUEST_CODE, Activity.RESULT_OK)

        verify(targetMock).updateProducts(response)
    }

    @Test
    fun `does not update products list on different request code of activity result`() {
        whenever(productRepository.getDatabaseProducts(any(), any()))
            .thenReturn(Single.just(response))

        presenter.onActivityResult(2, Activity.RESULT_OK)

        verify(targetMock, never()).updateProducts(response)
        verify(productRepository, never()).getDatabaseProducts(any(), any())
    }


    @Test
    fun `does not update products list on different activity result`() {
        whenever(productRepository.getDatabaseProducts(any(), any()))
            .thenReturn(Single.just(response))

        presenter.onActivityResult(OpenedProductActivity.REQUEST_CODE, Activity.RESULT_CANCELED)

        verify(targetMock, never()).updateProducts(response)
        verify(productRepository, never()).getDatabaseProducts(any(), any())
    }

    @Test
    fun `calculates correct page limit for database request on activity result`() {
        whenever(productRepository.getDatabaseProducts(any(), any()))
            .thenReturn(Single.just(response))
        whenever(productRepository.getProducts(any())).thenReturn(Single.just(response))

        presenter.onCreate()
        presenter.onListEndReached()
        presenter.onActivityResult(OpenedProductActivity.REQUEST_CODE, Activity.RESULT_OK)

        verify(productRepository).getDatabaseProducts(2 * ProductsRepository.PAGE_LIMIT, 0)
    }

    private fun setsProducts(callFunction: () -> Unit) {
        whenever(productRepository.getProducts(any())).thenReturn(Single.just(response))

        callFunction()

        verify(targetMock).addProducts(response)
    }

    private fun loadsProducts(callFunction: () -> Unit) {
        whenever(productRepository.getProducts(any())).thenReturn(Single.just(response))

        callFunction()

        verify(productRepository).getProducts(0)
    }

    private fun showsErrorPopUp(callFunction: () -> Unit) {
        whenever(productRepository.getProducts(any())).thenReturn(Single.error(NoInternetException()))

        callFunction()

        verify(targetMock).showInternetErrorPopUp()
    }

}