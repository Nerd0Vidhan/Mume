package com.lokal.mume.presentation.utils


sealed class UiState<out T> {
    object Nothing : UiState<kotlin.Nothing>()
    object Loading : UiState<kotlin.Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<kotlin.Nothing>()
}