package com.ljb.domain

/**
 * API 호출 상태 sealed class
 * */
sealed class NetworkState<out T>{
    object Loading: NetworkState<Nothing>()
    data class Success<out T>(val data: T): NetworkState<T>()
    data class Error(val message:String?): NetworkState<Nothing>()
}
