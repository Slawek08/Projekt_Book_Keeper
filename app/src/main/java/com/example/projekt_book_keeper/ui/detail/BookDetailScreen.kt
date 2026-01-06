package com.example.projekt_book_keeper.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons


import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.example.projekt_book_keeper.domain.model.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    book: Book,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    viewModel: BookDetailViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(book.title, fontWeight = FontWeight.Bold,fontStyle = FontStyle.Italic, style = MaterialTheme.typography.titleLarge) },
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
                },
                actions = {
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(Color(0xFFFFCC80))
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CompositionLocalProvider(
                LocalContentColor provides Color(0xFF4E342E)
            ) {
            AsyncImage(
                model = book.bigCoverUrl,
                contentDescription = book.title
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = book.title, fontWeight = FontWeight.Bold,fontStyle = FontStyle.Italic, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Autor: ${book.author}", fontWeight = FontWeight.Bold,fontStyle = FontStyle.Italic, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Publish year: ${book.publishYear}", fontWeight = FontWeight.Bold,fontStyle = FontStyle.Italic, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LaunchedEffect(book.key) {
                viewModel.load(book)
            }
            val fullBook by viewModel.book.collectAsState()

            if (fullBook == null) {
                Text("Loading details ...")
            } else {
                Text("Pages: ${fullBook?.pages ?: "No data about pages"}", fontWeight = FontWeight.Bold,fontStyle = FontStyle.Italic, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Description:", fontWeight = FontWeight.Bold,fontStyle = FontStyle.Italic, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                Text(fullBook?.description ?: "No data about description", fontWeight = FontWeight.Bold,fontStyle = FontStyle.Italic, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            }
        }
        }
    }
}