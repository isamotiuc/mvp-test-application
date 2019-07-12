package com.isamotiuc.ebstestapplication.favorite_list.data

import com.isamotiuc.ebstestapplication.BaseTestCase
import com.isamotiuc.ebstestapplication.products_list.data.Product
import com.isamotiuc.ebstestapplication.products_list.data.ProductsDAO
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock

class FavoritesRepositoryTest : BaseTestCase() {
    @InjectMocks
    lateinit var favoritesRepository: FavoritesRepository

    @Mock
    lateinit var productsDAO: ProductsDAO

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
    fun `gets favorites`() {
        whenever(productsDAO.getFavorites()).thenReturn(Single.just(response))

        val testObserver = favoritesRepository.getFavorites()
            .test().apply {
                awaitTerminalEvent()
            }

        testObserver
            .assertNoErrors()
            .assertValue { products ->
                products == response
            }
    }

    @Test
    fun `updates favorite`() {
        whenever(productsDAO.updateFavorite(any(), any())).thenReturn(Single.just(0))

        val testObserver = favoritesRepository.updateFavorite(response[0].id, true)
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