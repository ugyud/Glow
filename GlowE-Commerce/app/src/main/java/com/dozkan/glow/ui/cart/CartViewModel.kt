package com.dozkan.glow.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dozkan.glow.common.Resource
import com.dozkan.glow.data.model.*
import com.dozkan.glow.data.model.request.ClearCartRequest
import com.dozkan.glow.data.model.request.DeleteFromCartRequest
import com.dozkan.glow.data.model.response.BaseResponse
import com.dozkan.glow.data.model.response.ProductUI
import com.dozkan.glow.data.repository.ProductRepository
import com.dozkan.glow.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
    ) : ViewModel() {

    private var _cartState = MutableLiveData<CartState>()
    val cartState: LiveData<CartState>
        get() = _cartState

    private val _totalPriceAmount = MutableLiveData(0.0)
    val totalPriceAmount: LiveData<Double> = _totalPriceAmount

    fun getCartProducts(userId: String) {
        viewModelScope.launch {
            val result = productRepository.getCartProducts(userId)

            when(result) {
                is Resource.Success -> {
                    _cartState.value = CartState.Data(result.data)
                    _totalPriceAmount.value = result.data.sumOf { product ->
                        if (product.saleState) {
                            product.price
                        } else {
                            product.price
                        }
                    }
                }
                is Resource.Error -> {
                    _cartState.value = CartState.Error(result.throwable)
                    resetTotalAmount()
                }

                is Resource.Fail -> TODO()
            }
        }
    }

    fun deleteFromCart(id: Int) {
        viewModelScope.launch {
            val deleteFromCartRequest = DeleteFromCartRequest(id)
            when(val result = productRepository.deleteFromCart(deleteFromCartRequest)) {
                is Resource.Success -> {
                    _cartState.value = CartState.DeleteFromCart(result.data)
                    getCartProducts(userRepository.getUserUid())
                }
                is Resource.Error -> {
                    _cartState.value = CartState.Error(result.throwable)
                }

                is Resource.Fail -> TODO()
            }
        }
    }

    fun clearCart(userId: String) {
        viewModelScope.launch {
            val clearCartRequest = ClearCartRequest(userId)
            when(val result = productRepository.clearCart(clearCartRequest)) {
                is Resource.Success -> {
                    _cartState.value = CartState.ClearCart(result.data)
                    getCartProducts(userRepository.getUserUid())
                }
                is Resource.Error -> {
                    _cartState.value = CartState.Error(result.throwable)
                }

                is Resource.Fail -> TODO()
            }
        }
    }

    fun increase(price: Double?) {
        _totalPriceAmount.value = price?.let { _totalPriceAmount.value?.plus(it) }
    }

    fun decrease(price: Double?) {
        _totalPriceAmount.value = price?.let { _totalPriceAmount.value?.minus(it) }
    }

    private fun resetTotalAmount() {
        _totalPriceAmount.value = 0.0
    }

}

sealed interface CartState {
    data class Data(val products: List<ProductUI>) : CartState
    data class Error(val throwable: Throwable) : CartState
    data class DeleteFromCart(val baseResponse: BaseResponse) : CartState
    data class ClearCart(val baseResponse: BaseResponse) : CartState
}