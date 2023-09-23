package com.dozkan.glow.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dozkan.glow.common.Resource
import com.dozkan.glow.data.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val userRepo: UserRepository) : ViewModel() {

    private var _signInState = MutableLiveData<SignInState>()
    val signInState: LiveData<SignInState>
        get() = _signInState

    val currentUser: FirebaseUser?
        get() = userRepo.currentUser

    fun signUpUser(email: String, password: String) {
        viewModelScope.launch {
            val result = userRepo.signUp(email, password)

            if (result is Resource.Success) {
                _signInState.value = SignInState.Data(result.data)
            } else if (result is Resource.Error) {
                _signInState.value = SignInState.Error(result.throwable)
            }
        }
    }


    fun signInUser(email: String, password: String) {
        viewModelScope.launch {
            val result = userRepo.signIn(email, password)

            if (result is Resource.Success) {
                _signInState.value = SignInState.Data(result.data)
            } else if (result is Resource.Error) {
                _signInState.value = SignInState.Error(result.throwable)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepo.logout()
        }
    }
}

sealed interface SignInState {
    data class Data(val user: Boolean) :  SignInState
    data class Error(val throwable: Throwable) :  SignInState
}