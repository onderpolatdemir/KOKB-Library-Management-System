package com.kokb.lms.features.mybooks.data.repository

import com.kokb.lms.core.manager.BookManager
import com.kokb.lms.features.borrow.domain.model.BorrowStatus
import com.kokb.lms.features.mybooks.data.remote.MyBooksApi
import com.kokb.lms.features.mybooks.domain.model.UserBorrow
import com.kokb.lms.features.mybooks.domain.repository.MyBooksRepository
import javax.inject.Inject
import android.util.Log

class MyBooksRepositoryImpl @Inject constructor(
    private val api: MyBooksApi,
    private val bookManager: BookManager
) : MyBooksRepository {
    override suspend fun getUserBorrows(userId: String): Result<List<UserBorrow>> {
        return try {
            Log.d("MyBooksDebug", "Repository - Fetching borrows for user: $userId")
            val response = api.getUserBorrows(userId)
            Log.d("MyBooksDebug", "Repository - Received ${response.size} borrows from API")
            
            val books = bookManager.getBooks() // Get all books
            Log.d("MyBooksDebug", "Repository - Retrieved ${books.size} books from BookManager")
            
            Result.success(
                response.map { dto ->
                    UserBorrow(
                        id = dto.id,
                        bookId = dto.bookId,
                        userId = dto.userId,
                        borrowedAt = dto.borrowedAt,
                        returnedAt = dto.returnedAt,
                        dueDate = dto.dueDate,
                        lateFee = dto.lateFee,
                        status = when (dto.status) {
                            0 -> BorrowStatus.BORROWED
                            1 -> BorrowStatus.RETURNED
                            2 -> BorrowStatus.OVERDUE
                            else -> throw IllegalStateException("Unknown borrow status: ${dto.status}")
                        },
                        book = books.find { it.id == dto.bookId }?.also { book ->
                            Log.d("MyBooksDebug", "Repository - Found book for borrow: ${book.title}")
                        } // Find and attach the book details
                    )
                }
            )
        } catch (e: Exception) {
            Log.e("MyBooksDebug", "Repository - Error fetching borrows", e)
            Result.failure(e)
        }
    }

    override suspend fun returnBook(transactionId: String): Result<Unit> {
        return try {
            Log.d("BorrowDebug", "Sending returnBook request with transactionId: $transactionId")

            api.returnBook(transactionId)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("MyBooksDebug", "Repository - Error returning book", e)
            Result.failure(e)
        }
    }
} 