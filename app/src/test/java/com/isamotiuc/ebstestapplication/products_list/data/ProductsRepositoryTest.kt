package com.isamotiuc.ebstestapplication.products_list.data

import com.isamotiuc.ebstestapplication.BaseTestCase
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock

class ProductsRepositoryTest : BaseTestCase() {
    @InjectMocks
    lateinit var productsRepository: ProductsRepository

    @Mock
    lateinit var productsDAO: ProductsDAO

    @Mock
    lateinit var apiInterface: ApiInterface

    private val networkResponse: List<Product> = listOf(
        Product(
            0, "test title", "test description",
            "image url", 100, 5, "test details", false
        ),
        Product(
            1, "test title 1", "test description 1",
            "image url 1", 1000, 0, "test details 1", false
        )
    )

    private val dbResponse: List<Product> = listOf(
        Product(
            0, "test title", "test description",
            "image url", 100, 5, "test details", true
        ),
        Product(
            1, "test title 1", "test description 1",
            "image url 1", 1000, 0, "test details 1", true
        )
    )

    @Test
    fun `gets product from server on success network request`() {
        whenever(apiInterface.getProducts(any(), any())).thenReturn(Single.just(networkResponse))
        whenever(productsDAO.getFavorites()).thenReturn(Single.just(dbResponse))

        val testObserver = productsRepository.getProducts(0)
            .test().apply {
                awaitTerminalEvent()
            }

        testObserver
            .assertNoErrors()
            .assertValue { products ->
                products == networkResponse
            }

    }

    @Test
    fun `gets product from DB on failed network request`() {
        whenever(apiInterface.getProducts(any(), any())).thenReturn(Single.error(Throwable()))
        whenever(productsDAO.getFavorites()).thenReturn(Single.just(dbResponse))
        whenever(productsDAO.getProducts(any(), any())).thenReturn(Single.just(dbResponse))

        val testObserver = productsRepository.getProducts(0)
            .test().apply {
                awaitTerminalEvent()
            }

        testObserver
            .assertNoErrors()
            .assertValue { products ->
                products == dbResponse
            }
    }

    @Test
    fun `throw internet error on failed network request and empty DB response`() {
        whenever(apiInterface.getProducts(any(), any())).thenReturn(Single.error(Throwable()))
        whenever(productsDAO.getFavorites()).thenReturn(Single.just(listOf()))
        whenever(productsDAO.getProducts(any(), any())).thenReturn(Single.just(listOf()))

        val testObserver = productsRepository.getProducts(0)
            .test().apply {
                awaitTerminalEvent()
            }

        testObserver
            .assertError(NoInternetException::class.java)
    }

    @Test
    fun `updates favorites field on network response`() {
        whenever(apiInterface.getProducts(any(), any())).thenReturn(Single.just(networkResponse))
        whenever(productsDAO.getFavorites()).thenReturn(Single.just(dbResponse))
        whenever(productsDAO.getProducts(any(), any())).thenReturn(Single.just(dbResponse))

        val testObserver = productsRepository.getProducts(0)
            .test().apply {
                awaitTerminalEvent()
            }

        testObserver
            .assertNoErrors()
            .assertValue { products ->
                products[0].favorite && products[1].favorite
            }
    }

    @Test
    fun `updates favorite`() {
        whenever(productsDAO.updateFavorite(any(), any())).thenReturn(Single.just(0))

        val testObserver = productsRepository.updateFavorite(networkResponse[0].id, true)
            .test().apply {
                awaitTerminalEvent()
            }

        testObserver
            .assertNoErrors()
            .assertValue {
                it == 0
            }
    }

    @Test
    fun `saves response from network in DB`() {
        whenever(apiInterface.getProducts(any(), any())).thenReturn(Single.just(networkResponse))
        whenever(productsDAO.getFavorites()).thenReturn(Single.just(dbResponse))

        productsRepository.getProducts(0)
            .test().apply {
                awaitTerminalEvent()
            }

        verify(productsDAO).insertAllProducts(networkResponse)
    }
}