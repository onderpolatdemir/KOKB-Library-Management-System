package com.kokb.lms.features.librarian.di

import com.kokb.lms.features.librarian.data.remote.LibrarianDashboardApi
import com.kokb.lms.features.librarian.data.repository.LibrarianDashboardRepositoryImpl
import com.kokb.lms.features.librarian.domain.repository.LibrarianDashboardRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LibrarianModule {

    companion object {
        @Provides
        @Singleton
        fun provideLibrarianDashboardApi(retrofit: Retrofit): LibrarianDashboardApi {
            return retrofit.create(LibrarianDashboardApi::class.java)
        }
    }

    @Binds
    @Singleton
    abstract fun bindLibrarianDashboardRepository(
        impl: LibrarianDashboardRepositoryImpl
    ): LibrarianDashboardRepository
} 