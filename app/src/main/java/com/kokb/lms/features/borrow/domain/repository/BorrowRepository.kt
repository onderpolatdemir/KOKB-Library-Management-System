package com.kokb.lms.features.borrow.domain.repository

import com.kokb.lms.features.borrow.domain.model.BorrowTransaction
import java.util.UUID
 
interface BorrowRepository {
    suspend fun borrowBook(userId: String, bookId: UUID, dayCount: Int): Result<BorrowTransaction>
} 