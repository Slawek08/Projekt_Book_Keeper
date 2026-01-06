package com.example.projekt_book_keeper.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WorkDetailResponse(
    @Json(name = "description") val description: Any?
)

@JsonClass(generateAdapter = true)
data class EditionsResponse(
    @Json(name = "entries") val entries: List<EditionEntry>?
)

@JsonClass(generateAdapter = true)
data class EditionEntry(
    @Json(name = "number_of_pages") val pages: Int?
)
