package com.kokb.lms.features.librarian.data.repository

import com.kokb.lms.features.librarian.data.model.LibrarianDashboardDto
import com.kokb.lms.features.librarian.data.model.toDomain
import com.kokb.lms.features.librarian.data.remote.LibrarianDashboardApi
import com.kokb.lms.features.librarian.domain.model.LibrarianDashboard
import com.kokb.lms.features.librarian.domain.repository.LibrarianDashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LibrarianDashboardRepositoryImpl @Inject constructor(
    private val api: LibrarianDashboardApi
) : LibrarianDashboardRepository {

    override suspend fun getLibrarianDashboard(userId: String): Flow<LibrarianDashboard> = flow {
        try {
            val statistics = api.getLibrarianStatistics(userId)
            val profile = api.getLibrarianProfile(userId)

            val dashboardDto = LibrarianDashboardDto(
                id = userId,
                librarianId = userId,
                statistics = statistics
            )

            emit(dashboardDto.toDomain())
        } catch (e: Exception) {
            throw e
        }
    }
} 