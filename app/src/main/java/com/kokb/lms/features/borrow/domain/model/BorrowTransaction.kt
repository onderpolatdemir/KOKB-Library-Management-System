package com.kokb.lms.features.borrow.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class BorrowTransaction(
    val id: UUID,
    val bookId: UUID,
    val userId: String,
    val borrowedAt: LocalDateTime,
    val returnedAt: LocalDateTime?,
    val dueDate: LocalDateTime,
    val lateFee: Double,
    val status: BorrowStatus
) 