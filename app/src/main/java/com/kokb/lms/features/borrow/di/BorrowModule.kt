package com.kokb.lms.features.borrow.di

import com.kokb.lms.features.borrow.data.remote.BorrowApi
import com.kokb.lms.features.borrow.data.repository.BorrowRepositoryImpl
import com.kokb.lms.features.borrow.domain.repository.BorrowRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BorrowModule {

    @Provides
    @Singleton
    fun provideBorrowApi(retrofit: Retrofit): BorrowApi {
        return retrofit.create(BorrowApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBorrowRepository(api: BorrowApi): BorrowRepository {
        return BorrowRepositoryImpl(api)
    }
} 