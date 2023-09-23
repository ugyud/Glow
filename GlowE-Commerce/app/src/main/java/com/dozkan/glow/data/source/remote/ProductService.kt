package com.dozkan.glow.data.source.remote

import com.dozkan.glow.common.Constants.EndPoints.ADD_TO_CART
import com.dozkan.glow.common.Constants.EndPoints.CLEAR_CART
import com.dozkan.glow.common.Constants.EndPoints.DELETE_FROM_CART
import com.dozkan.glow.common.Constants.EndPoints.GET_CART_PRODUCTS
import com.dozkan.glow.common.Constants.EndPoints.GET_PRODUCTS
import com.dozkan.glow.common.Constants.EndPoints.GET_PRODUCT_DETAIL
import com.dozkan.glow.common.Constants.EndPoints.GET_SALE_PRODUCTS
import com.dozkan.glow.common.Constants.EndPoints.SEARCH_PRODUCT
import com.dozkan.glow.data.model.request.AddToCartRequest
import com.dozkan.glow.data.model.request.ClearCartRequest
import com.dozkan.glow.data.model.request.DeleteFromCartRequest
import com.dozkan.glow.data.model.response.BaseResponse
import com.dozkan.glow.data.model.response.GetCartProductsResponse
import com.dozkan.glow.data.model.response.GetProductDetailResponse
import com.dozkan.glow.data.model.response.GetProductsResponse
import com.dozkan.glow.data.model.response.GetSaleProductsResponse
import com.dozkan.glow.data.model.response.SearchProductResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductService {

    @Headers("store: glow")
    @GET(GET_PRODUCTS)
    suspend fun getProducts(): GetProductsResponse

    @Headers("store: glow")
    @GET(GET_SALE_PRODUCTS)
    suspend fun getSaleProducts() : GetSaleProductsResponse

    @Headers("store: glow")
    @GET(GET_PRODUCT_DETAIL)
    suspend fun getProductDetail(
        @Query("id") id: Int
    ) : GetProductDetailResponse

    @Headers("store: glow")
    @GET(SEARCH_PRODUCT)
    suspend fun searchProduct(
        @Query("query") query: String?
    ) : SearchProductResponse

    @Headers("store: glow")
    @POST(ADD_TO_CART)
    suspend fun addToCart(
        @Body addToCartRequest: AddToCartRequest
    ) : BaseResponse

    @Headers("store: glow")
    @GET(GET_CART_PRODUCTS)
    suspend fun getCartProducts(
        @Query("userId") userId : String?
    ) : GetCartProductsResponse

    @Headers("store: glow")
    @POST(DELETE_FROM_CART)
    suspend fun deleteFromCart (
        @Body deleteFromCartRequest: DeleteFromCartRequest
    ) : BaseResponse

    @Headers("store: glow")
    @POST(CLEAR_CART)
    suspend fun clearCart(
        @Body clearCartRequest: ClearCartRequest
    ) : BaseResponse
}