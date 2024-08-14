package com.johan.popular_movies.utils

fun String.toFullPosterURL() = "${URL.POSTER_URL}$this"

object URL {
    const val POSTER_URL = "https://image.tmdb.org/t/p/w500/"
}