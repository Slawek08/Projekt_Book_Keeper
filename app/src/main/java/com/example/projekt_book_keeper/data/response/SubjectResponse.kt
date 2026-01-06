package com.example.projekt_book_keeper.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubjectResponse(
    @Json(name = "works") val works: List<SubjectWorkDto>?
)

@JsonClass(generateAdapter = true)
data class SubjectWorkDto(
    @Json(name = "key") val key: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "authors") val authors: List<SubjectAuthorDto>?,
    @Json(name = "cover_id") val coverId: Int?,
    @Json(name = "first_publish_year") val publishYear: Int?
)

@JsonClass(generateAdapter = true)
data class SubjectAuthorDto(
    @Json(name = "name") val name: String?
)
