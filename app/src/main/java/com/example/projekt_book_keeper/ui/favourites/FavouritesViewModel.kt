package com.example.projekt_book_keeper.ui.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekt_book_keeper.data.local.FavoritesManager
import com.example.projekt_book_keeper.data.repository.BookRepository
import com.example.projekt_book_keeper.data.repository.ResultState
import com.example.projekt_book_keeper.domain.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: BookRepository,
    private val favoritesManager: FavoritesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState(isLoading = true))
    val uiState: StateFlow<FavoritesUiState> = _uiState

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.getBooks()) {
                is ResultState.Success -> {
                    val favoriteIds = favoritesManager.getFavoriteIds()
                    val favorites = result.data.filter { favoriteIds.contains(it.key) }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        favorites = favorites,
                        isEmpty = favorites.isEmpty()
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
                        favorites = emptyList(),
                        isEmpty = true
                    )
                }
                is ResultState.Loading -> {}
            }
        }
    }

    fun toggleFavorite(book: Book) {
        favoritesManager.toggleFavorite(book.key)
        loadFavorites()
    }
}
