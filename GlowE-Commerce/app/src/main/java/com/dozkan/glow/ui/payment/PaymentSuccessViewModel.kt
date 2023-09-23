package com.dozkan.glow.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dozkan.glow.common.Resource
import com.dozkan.glow.data.model.request.ClearCartRequest
import com.dozkan.glow.data.model.response.BaseResponse
import com.dozkan.glow.data.model.response.ProductUI
import com.dozkan.glow.data.repository.ProductRepository
import com.dozkan.glow.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentSuccessViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private var _successState = MutableLiveData<SuccessState>()
    val successState: LiveData<SuccessState>
        get() = _successState

    private val _totalPriceAmount = MutableLiveData(0.0)
    val totalPriceAmount: LiveData<Double> = _totalPriceAmount

    private fun getCartProducts(userId: String) {
        viewModelScope.launch {
            val result = productRepository.getCartProducts(userId)

            when(result) {
                is Resource.Success -> {
                    _successState.value = SuccessState.Data(result.data)
                    _totalPriceAmount.value = result.data.sumOf {
                        it.price ?: 0.0
                    }
                }
                is Resource.Error -> {
                    _successState.value = SuccessState.Error(result.throwable)
                    resetTotalAmount()
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
                    _successState.value = SuccessState.ClearCart(result.data)
                    getCartProducts(userRepository.getUserUid())
                }
                is Resource.Error -> {
                    _successState.value = SuccessState.Error(result.throwable)
                }
                is Resource.Fail -> {
                    TODO()
                }
            }
        }
    }

    private fun resetTotalAmount() {
        _totalPriceAmount.value = 0.0
    }
}

sealed interface SuccessState {
    data class Data(val products: List<ProductUI>) : SuccessState
    data class Error(val throwable: Throwable) : SuccessState
    data class ClearCart(val baseResponse: BaseResponse) : SuccessState
}