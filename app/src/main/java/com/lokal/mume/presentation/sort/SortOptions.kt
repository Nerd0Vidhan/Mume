package com.lokal.mume.presentation.sort
data class SortType(val label: String, val value: String)

val sortOptions = listOf(
    SortType("Ascending", "asc"),
    SortType("Descending", "desc"),
    SortType("Artist", "artist"),
    SortType("Album", "album"),
    SortType("Year", "year"),
    SortType("Date Added", "date_added"),
    SortType("Date Modified", "date_modified"),
    SortType("Composer", "composer")
)