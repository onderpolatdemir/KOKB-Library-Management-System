package com.kokb.lms.features.books.di

import com.kokb.lms.features.books.data.remote.BookApi
import com.kokb.lms.features.books.data.repository.BookRepositoryImpl
import com.kokb.lms.features.books.domain.repository.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookModule {

    @Provides
    @Singleton
    fun provideBookApi(retrofit: Retrofit): BookApi {
        return retrofit.create(BookApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBookRepository(bookApi: BookApi): BookRepository {
        return BookRepositoryImpl(bookApi)
    }
} 