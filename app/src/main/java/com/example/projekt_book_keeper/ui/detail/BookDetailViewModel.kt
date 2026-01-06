package com.example.projekt_book_keeper.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekt_book_keeper.data.repository.BookRepository
import com.example.projekt_book_keeper.domain.model.Book

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val repository: BookRepository
) : ViewModel() {

    private val _book = MutableStateFlow<Book?>(null)
    val book = _book.asStateFlow()

    fun load(book: Book) {
        viewModelScope.launch {
            _book.value = repository.getBookWithDetails(book)
        }
    }
}




