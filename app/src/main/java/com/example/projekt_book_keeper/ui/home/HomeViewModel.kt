package com.example.projekt_book_keeper.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.projekt_book_keeper.data.local.FavoritesManager
import com.example.projekt_book_keeper.data.repository.BookRepository
import com.example.projekt_book_keeper.data.repository.ResultState
import com.example.projekt_book_keeper.domain.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: BookRepository,
    private val favoritesManager: FavoritesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                isEmpty = false
            )

            when (val result = repository.getBooks()) {
                is ResultState.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }

                is ResultState.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        books = result.data,
                        isEmpty = result.data.isEmpty(),
                        favoriteIds = favoritesManager.getFavoriteIds()
                    )
                }

                is ResultState.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }

                is ResultState.Empty -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        books = emptyList(),
                        isEmpty = true,
                        favoriteIds = favoritesManager.getFavoriteIds()
                    )
                }
            }
        }
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                query = query
            )

            when (val result = repository.searchBooks(query)) {
                is ResultState.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        books = result.data,
                        isEmpty = result.data.isEmpty(),
                        favoriteIds = favoritesManager.getFavoriteIds()
                    )
                }

                is ResultState.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }

                is ResultState.Empty -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        books = emptyList(),
                        isEmpty = true,
                        favoriteIds = favoritesManager.getFavoriteIds()
                    )
                }

                is ResultState.Loading -> {}
            }
        }
    }

    fun toggleFavorite(book: Book) {
        favoritesManager.toggleFavorite(book.key)

        _uiState.value = _uiState.value.copy(
            favoriteIds = favoritesManager.getFavoriteIds()
        )
    }

    fun getBookByKey(key: String): Book? {
        return uiState.value.books.find { it.key == key }
    }
}
