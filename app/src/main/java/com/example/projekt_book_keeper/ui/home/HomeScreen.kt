package com.example.projekt_book_keeper.ui.home

import androidx.compose.foundation.background
import com.example.projekt_book_keeper.domain.model.Book

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projekt_book_keeper.ui.List.BookListItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onBookClick: (Book) -> Unit,
    onFavoritesClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadBooks()
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Explorer", fontWeight = FontWeight.Bold,fontStyle = FontStyle.Italic, style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4E342E),
                    titleContentColor = Color(0xFFFFF3E0),
                    actionIconContentColor = Color(0xFFFFF3E0)
                ),
                actions = {
                    IconButton(onClick = onFavoritesClick) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize().background(Color(0xFFFFCC80))
        ) {
            SearchBar(
                query = state.query,
                onQueryChange = { viewModel.searchBooks(it) },
            )

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "An error occurred: ${state.error}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.loadBooks() }) {
                                Text("Try again")
                            }
                        }
                    }
                }

                state.isEmpty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No results")
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.books) { book ->
                            BookListItem(
                                book = book,
                                isFavorite = state.favoriteIds.contains(book.key),
                                onClick = { onBookClick(book) },
                                onFavoriteClick = { viewModel.toggleFavorite(book) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        singleLine = true,
        label = { Text("Search for books") }
    )
}
