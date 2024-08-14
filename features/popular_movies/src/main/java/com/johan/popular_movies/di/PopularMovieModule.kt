package com.johan.popular_movies.di

import com.johan.popular_movies.model.PopularMovieRepository
import com.johan.popular_movies.model.PopularMovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PopularMovieModule {

    @Binds
    abstract fun bindPopularMovieRepository(impl: PopularMovieRepositoryImpl): PopularMovieRepository

}