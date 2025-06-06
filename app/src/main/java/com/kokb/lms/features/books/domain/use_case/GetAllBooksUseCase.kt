package com.kokb.lms.features.books.domain.use_case

import com.kokb.lms.core.util.Resource
import com.kokb.lms.features.books.domain.model.Book
import com.kokb.lms.features.books.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAllBooksUseCase @Inject constructor(
    private val repository: BookRepository
) {
    operator fun invoke(): Flow<Resource<List<Book>>> = flow {
        try {
            emit(Resource.Loading())
            val books = repository.getAllBooks()
            emit(Resource.Success(books))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
} 