package com.johan.network.services

import com.johan.network.model.MovieDetailResponse
import com.johan.network.model.PopularMovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MovieService {
    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: String = "1"
    ): PopularMovieResponse

    @GET("3/movie/{movieId}")
    suspend fun getMovieDetail(@Path("movieId") movieId: String): MovieDetailResponse
}