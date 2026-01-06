package com.example.projekt_book_keeper.ui.favourites

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.projekt_book_keeper.domain.model.Book
import com.example.projekt_book_keeper.ui.List.BookListItem
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onBookClick: (Book) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favourites") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4E342E),
                    titleContentColor = Color(0xFFFFF3E0),
                    actionIconContentColor = Color(0xFFFFF3E0),
                    navigationIconContentColor = Color(0xFFFFF3E0)
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(Color(0xFFFFCC80))
                .padding(16.dp)
                .fillMaxSize(),

        ){
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Box(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("An error occured: ${state.error}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.loadFavorites() }) {
                                Text("Try again")
                            }
                        }
                    }
                }

                state.isEmpty -> {
                    Box(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No favourites books")
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                    ) {
                        items(state.favorites) { book ->
                            BookListItem(
                                book = book,
                                isFavorite = true,
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

