package com.example.projekt_book_keeper.data.remote

import com.example.projekt_book_keeper.data.response.SearchResponse
import com.example.projekt_book_keeper.data.response.SubjectResponse
import com.example.projekt_book_keeper.domain.model.EditionsResponse
import com.example.projekt_book_keeper.domain.model.WorkDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApi {
// https://openlibrary.org/subjects/fiction.json?limit=20
    @GET("subjects/fiction.json")
    suspend fun getBooks(
        @Query("limit") limit: Int = 20
    ): SubjectResponse
// https://openlibrary.org/search.json?q={query}&limit=20
    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): SearchResponse
//  https://openlibrary.org/works/{work_id}.json
    @GET("works/{id}.json")
        suspend fun fetchBookDetails(
            @Path("id") workId: String
        ): WorkDetailResponse
//    https://openlibrary.org/works/{work_id}/editions.json?limit=1
    @GET("works/{id}/editions.json?limit=1")
        suspend fun getEditions(
            @Path("id") workId: String
        ): EditionsResponse
}

