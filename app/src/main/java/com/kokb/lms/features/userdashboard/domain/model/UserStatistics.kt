package com.kokb.lms.features.userdashboard.domain.model

data class UserStatistics(
    val totalBorrowed: Int,
    val notReturned: Int,
    val totalVisitors: Int,
    val borrowedBooks: Int,
    val overdueBooks: Int
) 