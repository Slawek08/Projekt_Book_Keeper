package com.example.projekt_book_keeper.data.local

import android.content.SharedPreferences

class FavoritesManager(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val KEY_FAVORITES = "favorites_keys"
    }

    fun getFavoriteIds(): Set<String> {
        return sharedPreferences.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
    }

    fun toggleFavorite(bookKey: String) {
        val current = getFavoriteIds().toMutableSet()

        if (current.contains(bookKey)) {
            current.remove(bookKey)
        } else {
            current.add(bookKey)
        }
        sharedPreferences.edit().putStringSet(KEY_FAVORITES, current).apply()
    }
}