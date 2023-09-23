package com.dozkan.glow.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dozkan.glow.R
import com.dozkan.glow.common.loadImage
import com.dozkan.glow.common.setStrikeThrough
import com.dozkan.glow.common.visible
import com.dozkan.glow.data.model.response.ProductUI
import com.dozkan.glow.databinding.ItemSearchBinding

class SearchProductAdapter(
    private val onProductClick: (Int) -> Unit,
    private val onFavoriteClick: (ProductUI) -> Unit,
) : ListAdapter<ProductUI, SearchProductAdapter.ProductViewHolder>(ProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick,
            onFavoriteClick
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) = holder.bind(getItem(position))

    class ProductViewHolder(
        private val binding: ItemSearchBinding,
        private val onProductClick: (Int) -> Unit,
        private val onFavoriteClick: (ProductUI) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) = with(binding) {
            tvName.text = product.title
            tvPrice.text = "${product.price} ₺"

            if (product.saleState) {
                tvSalePrice.text = "${product.salePrice} ₺"
                tvSalePrice.visible()
                tvPrice.setStrikeThrough()
            }

            if (product.isFavorite) {
                ivFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                ivFavorite.setImageResource(R.drawable.ic_unfav)
            }

            ivProduct.loadImage(product.imageOne)

            root.setOnClickListener {
                onProductClick(product.id)
            }

            ivFavorite.setOnClickListener {
                onFavoriteClick(product)
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
}