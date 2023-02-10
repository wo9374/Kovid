package com.ljb.domain

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Complete<out T>(val data:T) : UiState<T>()
    data class Fail(val message : String?) : UiState<Nothing>()
}
