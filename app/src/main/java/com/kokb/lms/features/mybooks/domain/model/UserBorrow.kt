package com.kokb.lms.features.mybooks.domain.model

import com.kokb.lms.features.books.domain.model.Book
import com.kokb.lms.features.borrow.domain.model.BorrowStatus
import java.time.LocalDateTime
import java.util.UUID

data class UserBorrow(
    val id: UUID,
    val bookId: UUID,
    val userId: String,
    val borrowedAt: LocalDateTime,
    val returnedAt: LocalDateTime?,
    val dueDate: LocalDateTime,
    val lateFee: Double,
    val status: BorrowStatus,
    val book: Book? = null // Will be populated when needed
) 