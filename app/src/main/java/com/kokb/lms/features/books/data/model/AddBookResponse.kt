package com.kokb.lms.features.books.data.model

import com.google.gson.annotations.SerializedName
import com.kokb.lms.features.books.domain.model.Book
import com.kokb.lms.features.books.domain.model.BookStatus
import java.time.LocalDateTime
import java.util.UUID

data class AddBookResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("isbn")
    val isbn: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("genre")
    val genre: String,
    @SerializedName("publishDate")
    val publishDate: String,
    @SerializedName("copies")
    val copies: Int,
    @SerializedName("status")
    val status: Int
) {
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