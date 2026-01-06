package com.example.projekt_book_keeper.domain.model

data class Book(
    val key: String,
    val title: String,
    val author: String,
    val coverId: Int?,
    val publishYear: Int?,
    val pages: Int?,
    val description: String?
){
    val coverUrl: String?
        get() = coverId?.let { "https://covers.openlibrary.org/b/id/${it}-M.jpg" }

    val bigCoverUrl: String?
        get() = coverId?.let { "https://covers.openlibrary.org/b/id/${it}-L.jpg" }
}