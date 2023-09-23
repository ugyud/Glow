package com.dozkan.glow.data.model.response

data class GetProductDetailResponse(
    val message: String?,
    val product: Product,
    val status: Int?
)