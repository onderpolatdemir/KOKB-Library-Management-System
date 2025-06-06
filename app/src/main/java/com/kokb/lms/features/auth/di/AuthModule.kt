package com.kokb.lms.features.auth.di

import android.content.Context
import com.kokb.lms.core.manager.AuthManager
import com.kokb.lms.features.auth.data.remote.AuthApi
import com.kokb.lms.features.auth.data.repository.AuthRepositoryImpl
import com.kokb.lms.features.auth.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserRepositoryModule {

//    @Provides
//    @Singleton
//    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl("http://10.0.2.2:8080/") // Android emulator için localhost
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

    @Provides
    fun provideAuthApiService(retrofit: Retrofit) : AuthApi{
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthManager(@ApplicationContext context: Context): AuthManager {
        return AuthManager(context)
    }

    @Provides
    fun provideUserRepository(
        userApi: AuthApi,
        authManager: AuthManager // ✅ ADD THIS PARAMETER
    ): AuthRepository = AuthRepositoryImpl(userApi, authManager)

//    @Provides
//    fun provideUserRepository(userApi: AuthApi) : AuthRepository = AuthRepositoryImpl(userApi, )

}