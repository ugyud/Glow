package com.dozkan.glow.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dozkan.glow.common.Resource
import com.dozkan.glow.data.model.request.AddToCartRequest
import com.dozkan.glow.data.model.response.BaseResponse
import com.dozkan.glow.data.model.response.ProductUI
import com.dozkan.glow.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {

    private var _detailState = MutableLiveData<DetailState>()
    val detailState: LiveData<DetailState>
        get() = _detailState


    fun getProductDetail(id:Int) {
        viewModelScope.launch {

            when(val result = productRepository.getProductDetail(id)) {
                is Resource.Success -> {
                    _detailState.value = DetailState.Data(result.data)
                }
                is Resource.Error -> {
                    _detailState.value = DetailState.Error(result.throwable)
                }

                is Resource.Fail -> TODO()
            }
        }
    }

    fun addToCart(addToCartRequest: AddToCartRequest) {
        viewModelScope.launch {
            when(val result = productRepository.addToCart(addToCartRequest)) {
                is Resource.Success -> {
                    _detailState.value = DetailState.AddToBag(result.data)
                }
                is Resource.Error -> {
                    _detailState.value = DetailState.Error(result.throwable)
                }

                is Resource.Fail -> TODO()
            }
        }
    }
}

sealed interface DetailState {
    data class Data(val product: ProductUI) : DetailState
    data class Error(val throwable: Throwable) : DetailState
    data class AddToBag(val baseResponse: BaseResponse) : DetailState
}