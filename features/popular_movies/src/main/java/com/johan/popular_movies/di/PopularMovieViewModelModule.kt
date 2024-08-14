package com.johan.popular_movies.di


import com.johan.network.di.IoDispatcher
import com.johan.popular_movies.model.PopularMovieRepository
import com.johan.popular_movies.ui.PopularMovieViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(ViewModelComponent::class)
object PopularMovieViewModelModule {

    @Provides
    fun providePopularMovieViewModel(
        repository: PopularMovieRepository,
        @IoDispatcher ioDispatcher: CoroutineContext
    ) = PopularMovieViewModel(repository, ioDispatcher)

}