package com.dozkan.glow.data.repository

import com.dozkan.glow.common.Resource
import com.dozkan.glow.data.mapper.mapToProductEntity
import com.dozkan.glow.data.mapper.mapToProductUI
import com.dozkan.glow.data.model.request.AddToCartRequest
import com.dozkan.glow.data.model.request.ClearCartRequest
import com.dozkan.glow.data.model.request.DeleteFromCartRequest
import com.dozkan.glow.data.model.response.BaseResponse
import com.dozkan.glow.data.model.response.ProductUI
import com.dozkan.glow.data.source.local.ProductDAO
import com.dozkan.glow.data.source.remote.ProductService


class ProductRepository(
    private val productService: ProductService,
    private val productDAO: ProductDAO
) {

    suspend fun getProducts(): Resource<List<ProductUI>> {
        return try {
            val favoriteList = getFavoritesList()
            val result = productService.getProducts().products.orEmpty()
            if(result.isEmpty()) {
                Resource.Error(Exception("Movies are not found!"))
            } else {
                Resource.Success(result.map {
                    it.mapToProductUI(isFavorite = favoriteList.contains(it.title))
                })
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getSaleProducts() : Resource<List<ProductUI>> {
        return try {
            val favoriteList = getFavoritesList()
            val result = productService.getSaleProducts().products.orEmpty()

            if(result.isEmpty()) {
                Resource.Error(Exception("Sale Movies are not found!"))
            } else {
                Resource.Success(result.map {
                    it.mapToProductUI(isFavorite = favoriteList.contains(it.title))
                })
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getProductDetail(id:Int) : Resource<ProductUI> {
        return try {

            val favoriteNamesList = getFavoritesList()
            val result = productService.getProductDetail(id).product
            result.let{
                Resource.Success(it.mapToProductUI(isFavorite = favoriteNamesList.contains(it.title)))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun searchProduct(query:String?) : Resource<List<ProductUI>> {
        return try {
            val favoriteList = getFavoritesList()
            val result = productService.searchProduct(query).products
            result?.let{
                Resource.Success(result.map {
                    it.mapToProductUI(isFavorite = favoriteList.contains(it.title))
                })
            } ?: kotlin.run {
                Resource.Error(Exception("There is no such a movie!"))
            }
        } catch(e:Exception) {
            Resource.Error(e)
        }
    }

    suspend fun addToCart(addToCartRequest: AddToCartRequest) : Resource<BaseResponse> {
        return try {
            val result = productService.addToCart(addToCartRequest)

            if(result.status==200) {
                Resource.Success(result)
            } else {
                Resource.Error(Exception(result.message.toString()))
            }
        } catch(e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun deleteFromCart(deleteFromCartRequest: DeleteFromCartRequest) : Resource<BaseResponse> {
        return try {
            val result = productService.deleteFromCart(deleteFromCartRequest)

            if(result.status == 200) {
                Resource.Success(result)
            } else {
                Resource.Error(Exception(result.message.toString()))
            }
        } catch(e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun clearCart(clearCartRequest: ClearCartRequest) : Resource<BaseResponse> {
        return try {
            val result = productService.clearCart(clearCartRequest)

            if(result.status == 200) {
                Resource.Success(result)
            } else {
                Resource.Error(Exception(result.message.toString()))
            }
        } catch(e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getCartProducts(userId:String?) : Resource<List<ProductUI>> {
        return try {
            val favoriteList = getFavoritesList()
            val result = productService.getCartProducts(userId)

            if(result.status == 200) {
                Resource.Success(result.products.orEmpty().map {
                    it.mapToProductUI(isFavorite = favoriteList.contains(it.title))
                })
            } else {
                Resource.Error(Exception(result.message.orEmpty()))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    fun addToFavorites(product: ProductUI) {
        productDAO.addToFavorite(product.mapToProductEntity())
    }

    fun deleteFromFavorites(product: ProductUI) {
        productDAO.deleteFromFavorites(product.mapToProductEntity())
    }

    fun getFavoritesList() = productDAO.getFavoritesTitles()

    fun getFavoriteProducts(): Resource<List<ProductUI>> {
        return try {
            val result = productDAO.getProducts().map { it.mapToProductUI() }

            if(result.isEmpty()) {
                Resource.Error(Exception("There are no products here!"))
            } else {
                Resource.Success(result)
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}