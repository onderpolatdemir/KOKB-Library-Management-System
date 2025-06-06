package com.kokb.lms.features.mybooks.data.remote.dto

import java.time.LocalDateTime
import java.util.UUID

data class UserBorrowsResponseDto(
    val id: UUID,
    val bookId: UUID,
    val userId: String,
    val borrowedAt: LocalDateTime,
    val returnedAt: LocalDateTime?,
    val dueDate: LocalDateTime,
    val lateFee: Double,
    val status: Int
) 