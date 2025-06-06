package com.kokb.lms.features.userdashboard.data.repository

import com.kokb.lms.features.userdashboard.data.model.UserDashboardDto
import com.kokb.lms.features.userdashboard.data.model.toDomain
import com.kokb.lms.features.userdashboard.data.remote.UserDashboardApi
import com.kokb.lms.features.userdashboard.domain.model.UserDashboard
import com.kokb.lms.features.userdashboard.domain.repository.UserDashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserDashboardRepositoryImpl @Inject constructor(
    private val api: UserDashboardApi
) : UserDashboardRepository {

    override suspend fun getUserDashboard(userId: String): Flow<UserDashboard> = flow {
        try {
            val statistics = api.getUserStatistics(userId)
            //val loans = api.getUserLoans(userId)
            val profile = api.getUserProfile(userId)

            val dashboardDto = UserDashboardDto(
                name = profile.name,
                statistics = statistics,
                //loanItems = loans
            )

            emit(dashboardDto.toDomain())
        } catch (e: Exception) {
            throw e
        }
    }
} 