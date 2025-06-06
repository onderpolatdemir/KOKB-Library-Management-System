package com.kokb.lms.features.borrow.domain.use_case

import android.util.Log
import com.kokb.lms.features.borrow.domain.model.BorrowTransaction
import com.kokb.lms.features.borrow.domain.repository.BorrowRepository
import java.util.UUID
import javax.inject.Inject

class BorrowBookUseCase @Inject constructor(
    private val repository: BorrowRepository
) {
    suspend operator fun invoke(userId: String, bookId: UUID, dayCount: Int = 15): Result<BorrowTransaction> {
        Log.d("BorrowDebug", "UseCase - Starting borrow operation with dayCount: $dayCount")
        return repository.borrowBook(userId, bookId, dayCount).also { result ->
            result.fold(
                onSuccess = { transaction ->
                    Log.d("BorrowDebug", "UseCase - Borrow successful, transaction status: ${transaction.status}")
                },
                onFailure = { error ->
                    Log.e("BorrowDebug", "UseCase - Borrow failed", error)
                }
            )
        }
    }
} 