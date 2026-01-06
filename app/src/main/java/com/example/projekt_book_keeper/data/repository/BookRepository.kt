package com.example.projekt_book_keeper.data.repository


import com.example.projekt_book_keeper.data.remote.OpenLibraryApi
import com.example.projekt_book_keeper.domain.model.Book

class BookRepository(
    private val api: OpenLibraryApi
) {
    suspend fun getBooks(): ResultState<List<Book>> {
        var currentOffset = 0
        val limit = 20
        return try {
            val response = api.getBooks( limit = limit)
            val works = response.works.orEmpty()

            currentOffset += limit

            if (works.isEmpty()) {
                ResultState.Empty
            } else {
                val books = works.mapNotNull { dto ->
                    val key = dto.key ?: return@mapNotNull null
                    val title = dto.title ?: return@mapNotNull null
                    val author = dto.authors?.firstOrNull()?.name ?: "Unknown author"
                    val publishYear = dto.publishYear?: return@mapNotNull null
                    Book(
                        key = key,
                        title = title,
                        author = author,
                        coverId = dto.coverId,
                        publishYear = publishYear,
                        pages = null,
                        description = null

                    )
                }
                if (books.isEmpty()) ResultState.Empty else ResultState.Success(books)
            } } catch (e: Exception) {
                ResultState.Error(e.message ?: "Unknown error")
            } }
    suspend fun searchBooks(query: String): ResultState<List<Book>> {
        if (query.isBlank()) return getBooks()

        return try {
            val response = api.searchBooks(query = query, limit = 20)
            val docs = response.docs.orEmpty()

            if (docs.isEmpty()) {
                ResultState.Empty
            } else {
                val books = docs.mapNotNull { dto ->
                    val key = dto.key ?: return@mapNotNull null
                    val title = dto.title ?: return@mapNotNull null
                    val author = dto.authorNames?.firstOrNull() ?: "Unknown author"

                    Book(
                        key = key,
                        title = title,
                        author = author,
                        coverId = dto.coverId,
                        publishYear = null,
                        pages = null,
                        description = null

                    )
                }
                if (books.isEmpty()) ResultState.Empty else ResultState.Success(books)
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun fetchDescription(workId: String): String? {
        val response = api.fetchBookDetails(workId)
        return when (val desc = response.description) {
            is String -> desc
            is Map<*, *> -> desc["value"]?.toString()
            else -> null
        }
    }

    suspend fun fetchPages(workId: String): Int? {
        return api.getEditions(workId).entries?.firstOrNull()?.pages
    }

    suspend fun getBookWithDetails(book: Book): Book {
        val id = book.key.removePrefix("/works/")

        val description = fetchDescription(id)
        val pages = fetchPages(id)

        return book.copy(
            pages = pages,
            description = description
        )
    }

}