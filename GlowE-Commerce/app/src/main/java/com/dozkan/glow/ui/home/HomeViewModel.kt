package com.dozkan.glow.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dozkan.glow.common.Resource
import com.dozkan.glow.data.model.response.ProductUI
import com.dozkan.glow.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState

    fun getProducts() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            val result = productRepository.getProducts()
            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.Data(result.data)
                }
                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }

                is Resource.Fail -> TODO()
            }
        }
    }

     fun getSaleProducts() {
        viewModelScope.launch {
            val result = productRepository.getSaleProducts()

            when(result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.SaleData(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }

                is Resource.Fail -> TODO()
            }
        }
    }

    fun addToFavorites(product: ProductUI) {
        viewModelScope.launch {
            productRepository.addToFavorites(product)
        }
    }
}

sealed interface HomeState {
    object Loading: HomeState
    data class Data(val products: List<ProductUI>) : HomeState
    data class SaleData(val products: List<ProductUI>) : HomeState
    data class Error(val throwable: Throwable) : HomeState
}