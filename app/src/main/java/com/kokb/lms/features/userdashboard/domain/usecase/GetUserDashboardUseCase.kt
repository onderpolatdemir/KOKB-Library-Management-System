package com.kokb.lms.features.userdashboard.domain.usecase


import com.kokb.lms.features.userdashboard.domain.model.UserDashboard
import com.kokb.lms.features.userdashboard.domain.repository.UserDashboardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDashboardUseCase @Inject constructor(
    private val repository: UserDashboardRepository
) {
    suspend operator fun invoke(userId: String): Flow<UserDashboard> {
        return repository.getUserDashboard(userId)
    }
} 