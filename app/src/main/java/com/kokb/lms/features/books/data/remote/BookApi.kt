package com.kokb.lms.features.books.data.remote

import com.kokb.lms.features.books.data.model.AddBookRequest
import com.kokb.lms.features.books.data.model.AddBookResponse
import com.kokb.lms.features.books.data.model.UpdateBookRequest
import com.kokb.lms.features.books.data.remote.dto.BookDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.PUT

interface BookApi {
    @GET("api/v1/Book")
    suspend fun getAllBooks(): List<BookDto>

    @GET("api/v1/Book/{id}")
    suspend fun getBookById(@Path("id") id: String): BookDto

    @DELETE("api/v1/Book/{id}")
    suspend fun deleteBook(@Path("id") id: String): Response<Unit>

    @POST("api/v1/Book")
    suspend fun addBook(@Body request: AddBookRequest): Response<AddBookResponse>

    @PUT("api/v1/Book/{id}")
    suspend fun updateBook(@Path("id") id: String,@Body request: UpdateBookRequest): Response<Unit>
} 