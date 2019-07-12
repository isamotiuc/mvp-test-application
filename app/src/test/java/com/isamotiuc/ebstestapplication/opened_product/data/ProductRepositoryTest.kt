package com.isamotiuc.ebstestapplication.opened_product.data

import com.isamotiuc.ebstestapplication.BaseTestCase
import com.isamotiuc.ebstestapplication.products_list.data.ApiInterface
import com.isamotiuc.ebstestapplication.products_list.data.Product
import com.isamotiuc.ebstestapplication.products_list.data.ProductsDAO
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock

class ProductRepositoryTest : BaseTestCase() {
    @InjectMocks
    lateinit var productRepository: ProductRepository

    @Mock
    lateinit var productsDAO: ProductsDAO

    @Mock
    lateinit var apiInterface: ApiInterface

    private val response = Product(
        0, "test title", "test description",
        "image url", 100, 5, "test details", false
    )

    private val favorite = Product(
        0, "test title", "test description",
        "image url", 100, 5, "test details", true
    )

    @Test
    fun `gets product from server on success network request`() {
        whenever(apiInterface.getProduct(any())).thenReturn(Single.just(response))
        whenever(productsDAO.getFavorite(any())).thenReturn(Single.just(favorite))

        val testObserver = productRepository.getProduct(0)
            .test().apply {
                awaitTerminalEvent()
            }

        testObserver
            .assertNoErrors()
            .assertValue { product ->
                product == response
            }
    }

    @Test
    fun `gets product from DB on failed network request`() {
        whenever(apiInterface.getProduct(any())).thenReturn(Single.error(Throwable()))
        whenever(productsDAO.getProduct(any())).thenReturn(Single.just(response))
        whenever(productsDAO.getFavorite(any())).thenReturn(Single.just(favorite))

        val testObserver = productRepository.getProduct(0)
            .test().apply {
                awaitTerminalEvent()
            }

        testObserver
            .assertNoErrors()
            .assertValue { product ->
                product == response
            }
    }

    @Test
    fun `updates favorite field on network response`() {
        whenever(apiInterface.getProduct(any())).thenReturn(Single.just(response))
        whenever(productsDAO.getFavorite(any())).thenReturn(Single.just(favorite))

        val testObserver = productRepository.getProduct(0)
            .test().apply {
                awaitTerminalEvent()
            }

        testObserver
            .assertNoErrors()
            .assertValue { product ->
                product.favorite
            }
    }

    @Test
    fun `saves response from network in DB`() {
        whenever(apiInterface.getProduct(any())).thenReturn(Single.just(response))
        whenever(productsDAO.getFavorite(any())).thenReturn(Single.just(favorite))

        productRepository.getProduct(0)
            .test().apply {
                awaitTerminalEvent()
            }

        verify(productsDAO).insertProduct(response)
    }

    @Test
    fun `updates favorite`() {
        whenever(productsDAO.updateFavorite(any(), any())).thenReturn(Single.just(0))

        val testObserver = productRepository.updateFavorite(response.id, true)
            .test().apply {
                awaitTerminalEvent()
            }

        testObserver
            .assertNoErrors()
            .assertValue {
                it == 0
            }
    }
}