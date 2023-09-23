package com.dozkan.glow.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dozkan.glow.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashFragmentViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _state = MutableLiveData<SplashState>()
    val state: LiveData<SplashState>
        get() = _state

    fun checkUserLogin() = viewModelScope.launch {
        if (userRepository.checkUserLogin()) {
            _state.value = SplashState.GoToHome
        } else {
            _state.value = SplashState.GoToSignIn
        }
    }
}

sealed interface SplashState {
    object GoToHome : SplashState
    object GoToSignIn : SplashState
}