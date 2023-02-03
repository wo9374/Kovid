package com.ljb.domain

sealed class UiState {
    object Loading : UiState()
    object Complete : UiState()
    object Fail : UiState()
}
