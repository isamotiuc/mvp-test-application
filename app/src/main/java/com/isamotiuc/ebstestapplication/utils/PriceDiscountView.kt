package com.isamotiuc.ebstestapplication.utils

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.isamotiuc.ebstestapplication.R
import kotlinx.android.synthetic.main.price_layout.view.*

class PriceDiscountView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.price_layout, this, true)
    }

    fun setPrice(discount: Int, price: Int) {
        if (discount != 0) {
            old_price_text_view.visibility = View.VISIBLE
            old_price_text_view.paintFlags =
                old_price_text_view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            old_price_text_view.text = "$ $price,-"
            price_text_view.text = "$ ${price - (price * discount / 100)},-"
        } else {
            old_price_text_view.visibility = View.GONE
            price_text_view.text = "$ $price,-"
        }
    }
}