package com.johan.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Destinations(val route: String) {
    @Serializable
    data object PopularMovie: Destinations(route = "popular_movie")
}