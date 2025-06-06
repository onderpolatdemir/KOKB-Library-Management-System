package com.kokb.lms.features.borrow.data.repository

import android.util.Log
import com.kokb.lms.features.borrow.data.remote.BorrowApi
import com.kokb.lms.features.borrow.domain.model.BorrowStatus
import com.kokb.lms.features.borrow.domain.model.BorrowTransaction
import com.kokb.lms.features.borrow.domain.repository.BorrowRepository
import java.util.UUID
import javax.inject.Inject

class BorrowRepositoryImpl @Inject constructor(
    private val api: BorrowApi
) : BorrowRepository {

    override suspend fun borrowBook(userId: String, bookId: UUID, dayCount: Int): Result<BorrowTransaction> {
        return try {
            Log.d("BorrowDebug", "Repository - Creating borrow request - userId: $userId, bookId: $bookId, dayCount: $dayCount")
            Log.d("BorrowDebug", "Repository - Sending API request")
            val response = api.borrowBook(userId, bookId, dayCount)
            Log.d("BorrowDebug", """Repository - Full response:
                |id: ${response.id}
                |bookId: ${response.bookId}
                |userId: ${response.userId}
                |status: ${response.status}
                |borrowedAt: ${response.borrowedAt}
                |returnedAt: ${response.returnedAt}
                |dueDate: ${response.dueDate}
                |lateFee: ${response.lateFee}
            """.trimMargin())
            Log.d("BorrowDebug", "Repository - Received API response - id: ${response.id}, status: ${response.status}, borrowedAt: ${response.borrowedAt}")
            val borrowStatus = when (response.status) {
                0 -> BorrowStatus.BORROWED
                1 -> BorrowStatus.RETURNED
                2 -> BorrowStatus.OVERDUE
                else -> throw IllegalStateException("Unknown borrow status: ${response.status}")
            }
            Log.d("BorrowDebug", "Repository - Mapped status ${response.status} to $borrowStatus")
            Result.success(
                BorrowTransaction(
                    id = response.id,
                    bookId = response.bookId,
                    userId = response.userId,
                    borrowedAt = response.borrowedAt,
                    returnedAt = response.returnedAt,
                    dueDate = response.dueDate,
                    lateFee = response.lateFee,
                    status = borrowStatus
                )
            )
        } catch (e: Exception) {
            Log.e("BorrowDebug", "Repository - Error in borrowBook", e)
            Result.failure(e)
        }
    }
} 