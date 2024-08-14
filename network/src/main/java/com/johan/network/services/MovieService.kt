package com.johan.network.services

import com.johan.network.model.MovieDetailResponse
import com.johan.network.model.PopularMovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface MovieService {
    @GET("3/movie/popular")
    suspend fun getPopularMovies(@QueryMap queryParam: Map<String, String>): PopularMovieResponse

    @GET("3/movie/{movieId}")
    suspend fun getMovieDetail(@Path("movieId") movieId: String): MovieDetailResponse
}