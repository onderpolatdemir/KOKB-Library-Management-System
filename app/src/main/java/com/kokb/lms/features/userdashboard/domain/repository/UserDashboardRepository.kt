package com.kokb.lms.features.userdashboard.domain.repository

import com.kokb.lms.features.userdashboard.domain.model.UserDashboard
import kotlinx.coroutines.flow.Flow

interface UserDashboardRepository {
    suspend fun getUserDashboard(userId: String): Flow<UserDashboard>
} 