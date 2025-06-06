package com.kokb.lms.features.books.domain.model

import java.time.LocalDateTime
import java.util.UUID

enum class BookStatus {
    AVAILABLE,    // 0
    BORROWED,     // 1
    RESERVED      // 2
}

data class Book(
    val id: UUID,
    val isbn: String,
    val title: String,
    val author: String,
    val description: String,
    val genre: String,
    val publishDate: LocalDateTime,
    val copies: Int,
    val status: BookStatus
) 