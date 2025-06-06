package com.kokb.lms.features.librarian.data.model

import com.kokb.lms.features.librarian.domain.model.LibrarianDashboard
import com.kokb.lms.features.librarian.domain.model.LibraryStatistics

data class LibrarianDashboardDto(
    val id: String,
    val librarianId: String,
    val statistics: LibraryStatisticsDto
)

data class LibraryStatisticsDto(
    val totalBooks: Int,
    val borrowedBooks: Int,
    val activeMembers: Int,
    val overdueBooks: Int
)

fun LibraryStatisticsDto.toDomain(): LibraryStatistics {
    return LibraryStatistics(
        totalBooks = totalBooks,
        borrowedBooks = borrowedBooks,
        activeMembers = activeMembers,
        overdueBooks = overdueBooks
    )
}

fun LibrarianDashboardDto.toDomain(): LibrarianDashboard {
    return LibrarianDashboard(
        id = id,
        librarianId = librarianId,
        statistics = statistics.toDomain()
    )
} 