package com.isamotiuc.ebstestapplication.opened_product.presentation

import com.isamotiuc.ebstestapplication.PresenterTestCase
import com.isamotiuc.ebstestapplication.opened_product.data.ProductRepository
import com.isamotiuc.ebstestapplication.opened_product.view.OpenedProductTarget
import com.isamotiuc.ebstestapplication.products_list.data.Product
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock

class OpenedProductPresenterTest :
    PresenterTestCase<OpenedProductPresenter, OpenedProductTarget>() {
    override lateinit var targetMock: OpenedProductTarget
    override val targetClazz: Class<OpenedProductTarget> = OpenedProductTarget::class.java

    @InjectMocks
    override lateinit var presenter: OpenedProductPresenter

    @Mock
    lateinit var openedProductRepository: ProductRepository

    private val testProduct = Product(
        0, "test title", "test description",
        "image url", 100, 5, "test details", false
    )

    override fun setUp() {
        super.setUp()
        whenever(openedProductRepository.getProduct(any())).thenReturn(Single.just(testProduct))
    }

    @Test
    fun `loads product on create`() {
        presenter.onCreate(5)

        verify(openedProductRepository).getProduct(5)
    }

    @Test
    fun `sets product image on create`() {
        presenter.onCreate(5)

        verify(targetMock).setImage(testProduct.image)
    }

    @Test
    fun `sets title on create`() {
        presenter.onCreate(5)

        verify(targetMock).setTitle(testProduct.title)
    }

    @Test
    fun `sets sub title on create`() {
        presenter.onCreate(5)

        verify(targetMock).setSubTitle(testProduct.shortDescription)
    }

    @Test
    fun `sets price on create`() {
        presenter.onCreate(5)

        verify(targetMock).setPrice(testProduct.salePercent, testProduct.price)
    }

    @Test
    fun `sets description on create`() {
        presenter.onCreate(5)

        verify(targetMock).setDescription(testProduct.details)
    }

    @Test
    fun `sets favorite on create`() {
        presenter.onCreate(5)

        verify(targetMock).setFavorite(testProduct.favorite)
    }

    @Test
    fun `saves favorite on favorite button clicked`() {
        whenever(openedProductRepository.updateFavorite(any(), any())).thenReturn(Single.just(1))

        presenter.onCreate(5)
        presenter.onFavoriteItemChanged(true)

        verify(openedProductRepository).updateFavorite(testProduct.id, true)
    }
}