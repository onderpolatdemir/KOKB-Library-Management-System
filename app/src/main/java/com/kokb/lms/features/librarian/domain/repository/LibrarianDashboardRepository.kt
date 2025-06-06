package com.kokb.lms.features.librarian.domain.repository

import com.kokb.lms.features.librarian.domain.model.LibrarianDashboard
import kotlinx.coroutines.flow.Flow

interface LibrarianDashboardRepository {
    suspend fun getLibrarianDashboard(userId: String): Flow<LibrarianDashboard>
} 