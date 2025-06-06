package com.kokb.lms.features.borrow.data.remote

import android.util.Log
import com.kokb.lms.features.borrow.data.remote.dto.BorrowRequestDto
import com.kokb.lms.features.borrow.data.remote.dto.BorrowResponseDto
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.POST
import okio.Buffer

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body
        val buffer = Buffer()
        requestBody?.writeTo(buffer)
        
        Log.d("BorrowDebug", """
            |Request:
            |URL: ${request.url}
            |Method: ${request.method}
            |Headers: ${request.headers}
            |Body: ${buffer.readUtf8()}
        """.trimMargin())

        val response = chain.proceed(request)
        val responseBody = response.peekBody(Long.MAX_VALUE).string()
        Log.d("BorrowDebug", """
            |Response:
            |Code: ${response.code}
            |Message: ${response.message}
            |Body: $responseBody
        """.trimMargin())

        return response
    }
}

interface BorrowApi {
    @POST("api/v1/Borrow/borrow")
    suspend fun borrowBook(
        @retrofit2.http.Query("userId") userId: String,
        @retrofit2.http.Query("bookId") bookId: java.util.UUID,
        @retrofit2.http.Query("dayCount") dayCount: Int
    ): BorrowResponseDto
} 