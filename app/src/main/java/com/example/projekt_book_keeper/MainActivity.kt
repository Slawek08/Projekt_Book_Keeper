package com.example.projekt_book_keeper

import androidx.activity.compose.setContent


import androidx.navigation.navArgument
import com.example.projekt_book_keeper.data.local.FavoritesManager
import com.example.projekt_book_keeper.data.remote.RetrofitInstance
import com.example.projekt_book_keeper.data.repository.BookRepository
import com.example.projekt_book_keeper.ui.detail.BookDetailScreen
import com.example.projekt_book_keeper.ui.favourites.FavoritesScreen
import com.example.projekt_book_keeper.ui.favourites.FavoritesViewModel
import com.example.projekt_book_keeper.ui.home.HomeScreen
import com.example.projekt_book_keeper.ui.home.HomeViewModel
import com.example.projekt_book_keeper.ui.navigation.NavRoutes
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.compose.runtime.getValue
import com.example.projekt_book_keeper.ui.detail.BookDetailViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val repository = BookRepository(RetrofitInstance.api)
            val sharedPrefs = getSharedPreferences("books_prefs", Context.MODE_PRIVATE)
            val favoritesManager = FavoritesManager(sharedPrefs)

            val homeViewModel: HomeViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return HomeViewModel(repository, favoritesManager) as T
                    }
                }
            )

            val favoritesViewModel: FavoritesViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return FavoritesViewModel(repository, favoritesManager) as T
                    }
                }
            )

            val bookDetailViewModel: BookDetailViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return BookDetailViewModel(repository) as T
                    }
                }
            )


            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = NavRoutes.HOME
            ) {
                composable(NavRoutes.HOME) {
                    HomeScreen(
                        viewModel = homeViewModel,
                        onBookClick = { book ->
                            navController.navigate("${NavRoutes.DETAIL}/${Uri.encode(book.key)}")
                        },
                        onFavoritesClick = {
                            navController.navigate(NavRoutes.FAVORITES)
                        },
                        isRefreshing = false,
                        onRefresh = { homeViewModel.loadBooks() }
                    )
                }

                composable(
                    route = "${NavRoutes.DETAIL}/{bookKey}",
                    arguments = listOf(
                        navArgument("bookKey") { type = NavType.StringType }
                    )
                ) { backStackEntry ->

                    val state by homeViewModel.uiState.collectAsState()

                    val encodedKey = backStackEntry.arguments?.getString("bookKey") ?: ""
                    val bookKey = Uri.decode(encodedKey)
                    val book = homeViewModel.getBookByKey(bookKey)

                    if (book == null) {
                        navController.popBackStack()
                        return@composable
                    }

                    BookDetailScreen(
                        book = book,
                        isFavorite = state.favoriteIds.contains(book.key),
                        onBack = { navController.popBackStack() },
                        onToggleFavorite = {
                            homeViewModel.toggleFavorite(book)
                        },
                        viewModel = bookDetailViewModel
                    )
                }


                composable(NavRoutes.FAVORITES) {
                    FavoritesScreen(
                        viewModel = favoritesViewModel,
                        onBookClick = { book ->
                            navController.navigate("${NavRoutes.DETAIL}/${Uri.encode(book.key)}")
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
