package com.example.projekt_book_keeper.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "docs") val docs: List<SearchDocDto>?
)

@JsonClass(generateAdapter = true)
data class SearchDocDto(
    @Json(name = "key") val key: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "author_name") val authorNames: List<String>?,
    @Json(name = "cover_i") val coverId: Int?,
)


