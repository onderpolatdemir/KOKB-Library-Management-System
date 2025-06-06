package com.kokb.lms.features.librarian.domain.model

data class LibrarianDashboard(
    val id: String,
    val librarianId: String,
    val statistics: LibraryStatistics
)

data class LibraryStatistics(
    val totalBooks: Int,
    val borrowedBooks: Int,
    val activeMembers: Int,
    val overdueBooks: Int
) 