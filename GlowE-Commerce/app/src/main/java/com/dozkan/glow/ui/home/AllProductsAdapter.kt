package com.dozkan.glow.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dozkan.glow.R
import com.dozkan.glow.common.loadImage
import com.dozkan.glow.data.model.response.ProductUI
import com.dozkan.glow.databinding.ItemAllProductsBinding

class AllProductsAdapter(
    private val productListener: ProductListener
) : ListAdapter<ProductUI, AllProductsAdapter.MovieViewHolder>(ProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
        ItemAllProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false), productListener
        )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MovieViewHolder(
        private val binding: ItemAllProductsBinding,
        private val productListener: ProductListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductUI) = with(binding) {
            tvProductTitle.text = product.title
            txtViewPrice.text = "$ ${product.price}"
            tvRate.text = "${product.rate}"
            ivProductPoster.loadImage(product.imageOne)

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


            if (product.saleState) {
                tvSalePrice.text = "$ ${product.salePrice}"
                txtViewPrice.setTextColor(Color.parseColor("#FF0000"))
            }

            root.setOnClickListener {
                productListener.onProductClick(product.id)
            }
        }
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
        fun onProductClick(id:Int)
        fun onFavoriteButtonClick(product: ProductUI)
    }

}