package com.kokb.lms.features.search.domain.model

data class SearchFilters(
    val bookName: String = "",
    val authorName: String = "",
    val isbn: String = "",
    val category: BookCategory? = null,
    val availability: BookAvailability? = null
)

enum class BookCategory {
    KIDS,
    TOYS,
    MOVIES,
    COMPUTERS,
    BABY,
    GROCERY,
    ELECTRONICS,
    SHOES,
    BOOKS,
    SPORTS,
    INDUSTRIAL,
    OUTDOORS,
    BEAUTY,
    HEALTH,
    HOME,
    JEWELERY,
    TOOLS,
    MUSIC,
    GAMES,
    AUTOMOTIVE
}

enum class BookAvailability {
    AVAILABLE,
    RESERVED
} 