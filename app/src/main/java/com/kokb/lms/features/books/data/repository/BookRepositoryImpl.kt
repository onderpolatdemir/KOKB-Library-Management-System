package com.kokb.lms.features.books.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.kokb.lms.features.books.data.model.AddBookRequest
import com.kokb.lms.features.books.data.model.AddBookResponse
import com.kokb.lms.features.books.data.model.UpdateBookRequest
import com.kokb.lms.features.books.data.remote.BookApi
import com.kokb.lms.features.books.domain.model.Book
import com.kokb.lms.features.books.domain.model.BookStatus
import com.kokb.lms.features.books.domain.repository.BookRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val api: BookApi
) : BookRepository {
    override suspend fun getAllBooks(): List<Book> {
        return api.getAllBooks().map { it.toBook() }
    }

    override suspend fun getBookById(id: UUID): Book {
        return api.getBookById(id.toString()).toBook()
    }

    override suspend fun deleteBook(id: UUID): Boolean {
        val response = api.deleteBook(id.toString())
        return response.code() == 204
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addBook(
        isbn: String,
        title: String,
        author: String,
        genre: String,
        copies: Int
    ): Result<Book> {
        return try {
            val currentDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
            val request = AddBookRequest(
                isbn = isbn,
                title = title,
                author = author,
                description = "Description",
                genre = genre,
                publishDate = currentDate,
                copies = copies
            )
            
            val response = api.addBook(request)
            if (response.isSuccessful) {
                response.body()?.let { bookResponse ->
                    Result.success(bookResponse.toBook())
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to add book: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateBook(
        id: UUID,
        isbn: String,
        title: String,
        author: String,
        genre: String,
        copies: Int,
        status: BookStatus
    ): Result<Unit> {
        return try {
            val currentDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
            val request = UpdateBookRequest(
                id = id.toString(),
                isbn = isbn,
                title = title,
                author = author,
                description = "Description",
                genre = genre,
                publishDate = currentDate,
                copies = copies,
                status = status.ordinal
            )
            
            val response = api.updateBook(id.toString(), request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to update book: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun AddBookResponse.toBook(): Book {
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