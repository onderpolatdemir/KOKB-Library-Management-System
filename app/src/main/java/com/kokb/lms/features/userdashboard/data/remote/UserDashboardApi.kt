package com.kokb.lms.features.userdashboard.data.remote


import com.kokb.lms.features.userdashboard.data.model.UserLoanDto
import com.kokb.lms.features.userdashboard.data.model.UserStatisticsDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserDashboardApi {
    //    @GET("api/v1/Borrow/user/{userId}/statistics")
//    suspend fun getUserStatistics(@Path("userId") userId: String): UserStatisticsDto
    @GET("api/v1/User/statistics/{userId}")
    suspend fun getUserStatistics(@Path("userId") userId: String): UserStatisticsDto

    @GET("api/v1/Borrow/borrow")
    suspend fun getUserLoans(@Path("userId") userId: String): List<UserLoanDto>

    @GET("api/v1/User/{userId}")
    suspend fun getUserProfile(@Path("userId") userId: String): UserProfileDto
}

data class UserProfileDto(
    val name: String,
    val surname: String
) 