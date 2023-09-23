package com.dozkan.glow.data.model.request

data class AddToCartRequest(
    val userId: String,
    val productId: Int
)