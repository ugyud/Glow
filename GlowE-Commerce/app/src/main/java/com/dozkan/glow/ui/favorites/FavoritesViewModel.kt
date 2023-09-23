package com.dozkan.glow.ui.favorites

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
class FavoritesViewModel  @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {

    private var _favState = MutableLiveData<FavState>()
    val favState: LiveData<FavState>
        get() = _favState

    fun getFavoriteProducts() {
        viewModelScope.launch {
            val result = productRepository.getFavoriteProducts()
            if (result is Resource.Success) {
                _favState.value = FavState.Data(result.data)
            } else if (result is Resource.Error) {
                _favState.value = FavState.Error(result.throwable)
            }
        }
    }

    fun deleteFromFavorites(product: ProductUI) = viewModelScope.launch {
        productRepository.deleteFromFavorites(product)
        getFavoriteProducts()
    }


}

sealed interface FavState {
    data class Data(val products: List<ProductUI>) : FavState
    data class Error(val throwable: Throwable) : FavState
}