package com.kokb.lms.features.books.domain.repository

import com.kokb.lms.features.books.domain.model.Book
import com.kokb.lms.features.books.domain.model.BookStatus
import java.util.UUID
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getAllBooks(): List<Book>
    suspend fun getBookById(id: UUID): Book
    suspend fun deleteBook(id: UUID): Boolean
    suspend fun addBook(
        isbn: String,
        title: String,
        author: String,
        genre: String,
        copies: Int
    ): Result<Book>

    
    suspend fun updateBook(
        id: UUID,
        isbn: String,
        title: String,
        author: String,
        genre: String,
        copies: Int,
        status: BookStatus
    ): Result<Unit>
} 