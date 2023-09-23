package com.dozkan.glow.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dozkan.glow.common.loadImage
import com.dozkan.glow.data.model.response.ProductUI
import com.dozkan.glow.databinding.ItemProductBinding

class SaleProductsAdapter(
    private val productListener: ProductListener
) : ListAdapter<ProductUI, SaleProductsAdapter.SaleMovieViewHolder>(ProductDiffCallBack()) {

    class SaleMovieViewHolder(
        private val binding: ItemProductBinding,
        private val productListener: ProductListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductUI) = with(binding) {
            tvProductName.text = product.title
            tvPrice.text = "₺ ${product.price}"
            tvSalePrice.text = "\$ ${product.salePrice}"

            var isLiked = product.isFavorite

            btnFavorite.setOnClickListener {
                isLiked = !isLiked
                btnFavorite.apply {
                    if (isLiked) {
                        productListener.onFavoriteButtonClick(product)
                        playAnimation()
                    } else {
                        cancelAnimation()
                        progress = 0.0f
                    }
                }
            }

            if(product.isFavorite) {
                btnFavorite.playAnimation()
            }

            imageViewProduct.loadImage(product.imageOne)

            root.setOnClickListener {
                productListener.onSaleClick(product.id ?: 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleMovieViewHolder =
        SaleMovieViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent,false), productListener
        )

    override fun onBindViewHolder(holder: SaleMovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductDiffCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }

    interface ProductListener {
        fun onSaleClick(id:Int)
        fun onFavoriteButtonClick(product: ProductUI)
    }
}