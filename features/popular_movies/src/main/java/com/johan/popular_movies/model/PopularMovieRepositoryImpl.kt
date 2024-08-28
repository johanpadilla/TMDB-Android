package com.johan.popular_movies.model

import com.johan.network.NetworkResponse
import com.johan.network.model.PopularMovieResponse
import com.johan.network.services.MovieService
import javax.inject.Inject

class PopularMovieRepositoryImpl @Inject constructor(private val remoteDataSource: MovieService) :
    PopularMovieRepository {

    override suspend fun getPopularMovies(): NetworkResponse<PopularMovieResponse, Throwable> {
        return try {
            NetworkResponse.Success(remoteDataSource.getPopularMovies())
        }
        catch (ex: Exception) {
            NetworkResponse.NetworkError(ex.cause)
        }
    }
}