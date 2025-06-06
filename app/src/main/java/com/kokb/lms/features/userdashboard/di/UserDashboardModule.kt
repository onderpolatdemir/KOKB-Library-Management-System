package com.kokb.lms.features.userdashboard.di

import com.kokb.lms.features.userdashboard.data.repository.UserDashboardRepositoryImpl
import com.kokb.lms.features.userdashboard.data.remote.UserDashboardApi
import com.kokb.lms.features.userdashboard.domain.repository.UserDashboardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserDashboardModule {

    @Provides
    @Singleton
    fun provideUserDashboardApi(retrofit: Retrofit): UserDashboardApi {
        return retrofit.create(UserDashboardApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserDashboardRepository(
        api: UserDashboardApi
    ): UserDashboardRepository {
        return UserDashboardRepositoryImpl(api)
    }
} 