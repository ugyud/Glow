package com.dozkan.glow.data.model.response

data class GetSaleProductsResponse (
    val message: String?,
    val products: List<Product>?,
    val status: Int?
)
