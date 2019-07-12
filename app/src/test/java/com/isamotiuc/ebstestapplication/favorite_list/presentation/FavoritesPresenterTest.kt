package com.isamotiuc.ebstestapplication.favorite_list.presentation

import android.app.Activity
import com.isamotiuc.ebstestapplication.PresenterTestCase
import com.isamotiuc.ebstestapplication.favorite_list.data.FavoritesRepository
import com.isamotiuc.ebstestapplication.favorite_list.view.FavoritesListTarget
import com.isamotiuc.ebstestapplication.opened_product.view.OpenedProductActivity
import com.isamotiuc.ebstestapplication.products_list.data.Product
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock

class FavoritesPresenterTest : PresenterTestCase<FavoritesPresenter, FavoritesListTarget>() {
    override lateinit var targetMock: FavoritesListTarget
    override val targetClazz: Class<FavoritesListTarget> = FavoritesListTarget::class.java

    @InjectMocks
    override lateinit var presenter: FavoritesPresenter

    @Mock
    lateinit var favoritesRepository: FavoritesRepository

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
    fun `loads favorites on create`() {
        whenever(favoritesRepository.getFavorites()).thenReturn(Single.just(response))

        presenter.onCreate()


        verify(favoritesRepository).getFavorites()
    }

    @Test
    fun `add favorites on create`() {
        whenever(favoritesRepository.getFavorites()).thenReturn(Single.just(response))

        presenter.onCreate()

        verify(targetMock).addProducts(response)
    }

    @Test
    fun `shows empty view on empty response`() {
        whenever(favoritesRepository.getFavorites()).thenReturn(Single.just(listOf()))

        presenter.onCreate()

        verify(targetMock).showEmptyView()
    }

    @Test
    fun `hides empty view on response with data`() {
        whenever(favoritesRepository.getFavorites()).thenReturn(Single.just(response))

        presenter.onCreate()

        verify(targetMock).hideEmptyView()
    }

    @Test
    fun `delete favorite on click`() {
        whenever(favoritesRepository.updateFavorite(any(), any())).thenReturn(Single.just(1))
        whenever(favoritesRepository.getFavorites()).thenReturn(Single.just(response))

        presenter.onFavoriteItemDeleted(response[0], false)

        verify(favoritesRepository).updateFavorite(response[0].id, false)
    }

    @Test
    fun `updates  favorite list on delete`() {
        whenever(favoritesRepository.updateFavorite(any(), any())).thenReturn(Single.just(1))
        whenever(favoritesRepository.getFavorites()).thenReturn(Single.just(response))

        presenter.onFavoriteItemDeleted(response[0], false)

        verify(targetMock).updateProducts(response)
    }

    @Test
    fun `sets activity result on favorite delete`() {
        whenever(favoritesRepository.updateFavorite(any(), any())).thenReturn(Single.just(1))
        whenever(favoritesRepository.getFavorites()).thenReturn(Single.just(response))

        presenter.onFavoriteItemDeleted(response[0], false)

        verify(targetMock).setActivityResult(Activity.RESULT_OK)
    }

    @Test
    fun `opens product activity on click`() {
        presenter.onItemClicked(response[0])

        verify(targetMock).startOpenedProductActivity(response[0].id)
    }

    @Test
    fun `closes screen on favorite icon click`() {
        presenter.onFavoriteIconClicked()

        verify(targetMock).finish()
    }

    @Test
    fun `updates favorites on product activity result`() {
        whenever(favoritesRepository.getFavorites()).thenReturn(Single.just(response))

        presenter.onActivityResult(OpenedProductActivity.REQUEST_CODE, Activity.RESULT_OK)

        verify(targetMock).updateProducts(response)
    }

    @Test
    fun `does not update products list on different request code of activity result`() {
        whenever(favoritesRepository.getFavorites()).thenReturn(Single.just(response))

        presenter.onActivityResult(2, Activity.RESULT_OK)

        verify(targetMock, never()).updateProducts(response)
        verify(favoritesRepository, never()).getFavorites()
    }


    @Test
    fun `does not update products list on different activity result`() {
        whenever(favoritesRepository.getFavorites()).thenReturn(Single.just(response))

        presenter.onActivityResult(OpenedProductActivity.REQUEST_CODE, Activity.RESULT_CANCELED)

        verify(targetMock, never()).updateProducts(response)
        verify(favoritesRepository, never()).getFavorites()
    }
}