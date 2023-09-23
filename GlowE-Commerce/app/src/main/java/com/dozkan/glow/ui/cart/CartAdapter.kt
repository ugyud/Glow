package com.dozkan.glow.ui.cart

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dozkan.glow.R
import com.dozkan.glow.common.loadImage
import com.dozkan.glow.data.model.response.ProductUI
import com.dozkan.glow.databinding.ItemCartProductBinding

class CartAdapter(
    private val productListener: ProductListener
) : ListAdapter<ProductUI, CartAdapter.CartItemViewHolder>(ProductDiffCallBack()) {

    class CartItemViewHolder(
        private val binding: ItemCartProductBinding,
        private val productListener: ProductListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) = with(binding) {
            var productCount = 1

            tvTitle.text = product.title
            ivProductPoster.loadImage(product.imageOne)
            productPrice.text = "$ ${product.price}"
            productAmountBasket.text = productCount.toString()

            if (product.saleState==true) {
                productSalePrice.text = "$ ${product.salePrice}"
                productPrice.setTextColor(Color.parseColor("#FF0000"))
            }

            btnDelete.setOnClickListener {
                productListener.onDeleteItemClick(product.id)
            }

            root.setOnClickListener {
                productListener.onCartItemClick(product.id)
            }

            btnPlusFood.setOnClickListener {
                productListener.onIncreaseClick(product.price)
                productCount++
                productAmountBasket.text = productCount.toString()
            }

            btnMinusFood.setOnClickListener {
                if(productCount != 1){
                    productListener.onDecreaseClick(product.price)
                    productCount--
                    productAmountBasket.text = productCount.toString()
                } else {
                    productListener.onDeleteItemClick(product.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        return CartItemViewHolder(
            ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false), productListener
        )
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
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
        fun onCartItemClick(id:Int)
        fun onDeleteItemClick(id:Int)
        fun onIncreaseClick(price:Double?)
        fun onDecreaseClick(price:Double?)
    }
}

