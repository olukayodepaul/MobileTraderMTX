package com.mobile.mobiletradermtx.util


sealed class NetworkResult<out T> {
    object Loading : NetworkResult<Nothing>()
    data class Error(val throwable: Throwable? = null) : NetworkResult<Nothing>()
    data class Success<out T>(val data: T? = null) : NetworkResult<T>()
    object Empty : NetworkResult<Nothing>()
}





