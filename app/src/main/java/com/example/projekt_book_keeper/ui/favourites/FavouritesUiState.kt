package com.example.projekt_book_keeper.ui.favourites

import com.example.projekt_book_keeper.domain.model.Book

data class FavoritesUiState(
    val isLoading: Boolean = false,
    val favorites: List<Book> = emptyList(),
    val error: String? = null,
    val isEmpty: Boolean = false
)