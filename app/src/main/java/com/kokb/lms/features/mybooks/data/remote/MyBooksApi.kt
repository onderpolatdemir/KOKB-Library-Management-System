package com.kokb.lms.features.mybooks.data.remote

import com.kokb.lms.features.mybooks.data.remote.dto.UserBorrowsResponseDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.Query
 
interface MyBooksApi {
    @GET("api/v1/Borrow/user/{userId}")
    suspend fun getUserBorrows(@Path("userId") userId: String): List<UserBorrowsResponseDto>

    @POST("api/v1/Borrow/return")
    suspend fun returnBook(@Query("transactionId") transactionId: String): Unit?
    //suspend fun returnBook(@Query("transactionId") transactionId: String): Unit?
} 