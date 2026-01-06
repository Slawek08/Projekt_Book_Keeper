package com.example.projekt_book_keeper.ui.home

import com.example.projekt_book_keeper.domain.model.Book

data class HomeUiState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val error: String? = null,
    val isEmpty: Boolean = false,
    val query: String = "",
    val favoriteIds: Set<String> = emptySet()
)
