package com.dozkan.glow.common

import com.dozkan.glow.data.model.response.ProductUI

sealed class Resource<out T : Any> {
    data class Success<out T : Any>(val data: T) : Resource<T>()
    data class Fail(val message: String) : Resource<Nothing>()
    data class Error(val throwable: Throwable) : Resource<Nothing>()
}
