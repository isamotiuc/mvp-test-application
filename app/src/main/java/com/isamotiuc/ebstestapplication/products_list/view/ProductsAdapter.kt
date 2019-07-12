package com.isamotiuc.ebstestapplication.products_list.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.isamotiuc.ebstestapplication.R
import com.isamotiuc.ebstestapplication.products_list.data.Product
import com.isamotiuc.ebstestapplication.utils.PriceDiscountView


class ProductsAdapter(
    val itemClickListener: (Product) -> Unit,
    val favoriteButtonClickListener: (Product, Boolean) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    private val mDiffer = AsyncListDiffer(this, ProductDiffUtilsCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_list_item, parent, false)
        return ProductsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(mDiffer.currentList[position], itemClickListener, favoriteButtonClickListener)
    }

    fun updateProducts(products: List<Product>) {
        mDiffer.submitList(products)
    }

    fun addProducts(products: List<Product>) {
        val newList = mDiffer.currentList + products
        mDiffer.submitList(newList)
    }

    class ProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var productImageView: ImageView = itemView.findViewById(R.id.product_image_view)
        var titleTextView: TextView = itemView.findViewById(R.id.title_text_view)
        var subTitleTextView: TextView = itemView.findViewById(R.id.sub_title_text_view)
        var priceDiscountView: PriceDiscountView = itemView.findViewById(R.id.price_discount_view)
        var favoriteToggleButton: ToggleButton = itemView.findViewById(R.id.favorite_toggle_button)

        @SuppressLint("SetTextI18n")
        fun bind(
            product: Product,
            clickListener: (Product) -> Unit,
            favoriteButtonClickListener: (Product, Boolean) -> Unit
        ) {
            Glide.with(itemView.context)
                .load(product.image)
                .placeholder(R.drawable.ic_placeholder_128dp)
                .into(productImageView)

            titleTextView.text = product.title
            subTitleTextView.text = product.shortDescription

            priceDiscountView.setPrice(product.salePercent, product.price)
            favoriteToggleButton.isChecked = product.favorite

            favoriteToggleButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed) {
                    product.favorite = isChecked
                    favoriteButtonClickListener(product, isChecked)
                }
            }
            itemView.setOnClickListener { clickListener(product) }
        }
    }
}

class ProductDiffUtilsCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}

