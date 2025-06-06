package com.kokb.lms.features.librarian.data.remote

import com.kokb.lms.features.librarian.data.model.LibraryStatisticsDto
import retrofit2.http.GET
import retrofit2.http.Path

interface LibrarianDashboardApi {
    @GET("api/v1/User/statistics/{userId}")
    suspend fun getLibrarianStatistics(@Path("userId") userId: String): LibraryStatisticsDto

    @GET("api/v1/User/{userId}")
    suspend fun getLibrarianProfile(@Path("userId") userId: String): LibrarianProfileDto
}

data class LibrarianProfileDto(
    val name: String,
    val surname: String
) 