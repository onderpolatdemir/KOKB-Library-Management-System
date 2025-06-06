package com.kokb.lms.features.userdashboard.domain.model

import java.util.UUID

data class UserLoan(
    val id: UUID,
    val bookTitle: String,
    val bookAuthor: String,
    val returnDate: String,
    val status: BorrowStatus,
    val lateFee: Double
)

enum class BorrowStatus {
    BORROWED,
    RETURNED,
    OVERDUE
} 