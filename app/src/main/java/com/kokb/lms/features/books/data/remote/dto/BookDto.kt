package com.kokb.lms.features.books.data.remote.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.kokb.lms.features.books.domain.model.Book
import com.kokb.lms.features.books.domain.model.BookStatus
import java.time.LocalDateTime
import java.util.UUID

data class BookDto(
    val id: String,
    val isbn: String,
    val title: String,
    val author: String,
    val description: String,
    val genre: String,
    val publishDate: String,
    val copies: Int,
    val status: Int
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toBook(): Book {
        val bookStatus = when (status) {
            0 -> BookStatus.AVAILABLE
            1 -> BookStatus.BORROWED
            2 -> BookStatus.RESERVED
            else -> throw IllegalStateException("Unknown borrow status: $status")
        }

        val parsedDate = LocalDateTime.parse(publishDate.substring(0, 19))
        val parsedId = UUID.fromString(id)

        return Book(
            id = parsedId,
            isbn = isbn,
            title = title,
            author = author,
            description = description,
            genre = genre,
            publishDate = parsedDate,
            copies = copies,
            status = bookStatus
        )
    }
} 