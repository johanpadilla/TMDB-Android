package com.johan.details_movie.di

import com.johan.details_movie.model.MovieDetailsRepository
import com.johan.details_movie.model.MovieDetailsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieDetailsModule {
    @Binds
    abstract fun bindMovieDetailRepository(impl: MovieDetailsRepositoryImpl): MovieDetailsRepository

}