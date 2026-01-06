package com.example.projekt_book_keeper.data.repository

sealed class ResultState<out T> {
    object Loading : ResultState<Nothing>()
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error(val message: String) : ResultState<Nothing>()
    object Empty : ResultState<Nothing>()
}