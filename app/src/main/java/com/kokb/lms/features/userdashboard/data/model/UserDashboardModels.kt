package com.kokb.lms.features.userdashboard.data.model


import com.kokb.lms.features.userdashboard.domain.model.BorrowStatus
import com.kokb.lms.features.userdashboard.domain.model.UserDashboard
import com.kokb.lms.features.userdashboard.domain.model.UserLoan
import com.kokb.lms.features.userdashboard.domain.model.UserStatistics
import java.util.UUID

data class UserLoanDto(
    val id: String,
    val bookTitle: String,
    val bookAuthor: String,
    val returnDate: String,
    val status: Int,
    val lateFee: Double
)

data class UserStatisticsDto(
    val totalBorrowed: Int,
    val notReturned: Int,
    val totalVisitors: Int,
    val borrowedBooks: Int,
    val overdueBooks: Int
)

data class UserDashboardDto(
    val name: String,
    val statistics: UserStatisticsDto,
    //val loanItems: List<UserLoanDto>
)

fun UserLoanDto.toDomain(): UserLoan {
    return UserLoan(
        id = UUID.fromString(id),
        bookTitle = bookTitle,
        bookAuthor = bookAuthor,
        returnDate = returnDate,
        status = when (status) {
            0 -> BorrowStatus.BORROWED
            1 -> BorrowStatus.RETURNED
            2 -> BorrowStatus.OVERDUE
            else -> BorrowStatus.BORROWED
        },
        lateFee = lateFee
    )
}

fun UserStatisticsDto.toDomain(): UserStatistics {
    return UserStatistics(
        totalBorrowed = totalBorrowed,
        notReturned = notReturned,
        totalVisitors = totalVisitors,
        borrowedBooks = borrowedBooks,
        overdueBooks = overdueBooks
    )
}

fun UserDashboardDto.toDomain(): UserDashboard {
    return UserDashboard(
        name = name,
        statistics = statistics.toDomain(),
        //loanItems = loanItems.map { it.toDomain() }
    )
}