package com.johan.details_movie.di

import com.johan.details_movie.model.MovieDetailsRepository
import com.johan.details_movie.model.MovieDetailsRepositoryImpl
import com.johan.details_movie.ui.MovieDetailsViewModel
import com.johan.network.di.IoDispatcher
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieDetailsModule {

    @Binds
    abstract fun bindMovieDetailRepository(impl: MovieDetailsRepositoryImpl): MovieDetailsRepository
}

@Module
@InstallIn(ViewModelComponent::class)
object MovieDetailsViewModelModule {

    @Provides
    fun provideMovieDetailsViewModel(
        repository: MovieDetailsRepository,
        @IoDispatcher ioDispatcher: CoroutineContext
    ) =
        MovieDetailsViewModel(repository, ioDispatcher)
}