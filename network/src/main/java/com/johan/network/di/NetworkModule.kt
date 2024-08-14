package com.johan.network.di

import com.johan.network.BuildConfig
import com.johan.network.interceptors.AuthenticationInterceptor
import com.johan.network.services.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl(): String = BuildConfig.API_URL

    @Provides
    fun authenticationInterceptor(): AuthenticationInterceptor = AuthenticationInterceptor()

    @Provides
    @Singleton
    fun provideRetrofit(
        baseUrl: String,
        authenticationInterceptor: AuthenticationInterceptor
    ): Retrofit = Retrofit.Builder()
        .client(
            OkHttpClient.Builder()
                .addNetworkInterceptor(authenticationInterceptor)
                .build()
        )
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    fun provideMovieService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)
}