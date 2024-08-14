package com.johan.popular_movies.model

import com.johan.network.services.MovieService
import javax.inject.Inject

class PopularMovieRepositoryImpl @Inject constructor(private val remoteDataSource: MovieService) :
    PopularMovieRepository {

    override suspend fun getPopularMovies(queryParam: Map<String, String>) =
        remoteDataSource.getPopularMovies(queryParam)
}