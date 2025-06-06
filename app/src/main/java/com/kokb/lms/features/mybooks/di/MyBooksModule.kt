package com.kokb.lms.features.mybooks.di

import com.kokb.lms.core.manager.BookManager
import com.kokb.lms.features.mybooks.data.remote.MyBooksApi
import com.kokb.lms.features.mybooks.data.repository.MyBooksRepositoryImpl
import com.kokb.lms.features.mybooks.domain.repository.MyBooksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyBooksModule {

    @Provides
    @Singleton
    fun provideMyBooksApi(retrofit: Retrofit): MyBooksApi {
        return retrofit.create(MyBooksApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMyBooksRepository(api: MyBooksApi, bookManager: BookManager): MyBooksRepository {
        return MyBooksRepositoryImpl(api, bookManager)
    }
} 