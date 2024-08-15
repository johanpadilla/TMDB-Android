package com.johan.details_movie.ui.composables

import androidx.compose.runtime.Composable
import com.johan.details_movie.model.Genre

@Composable
fun Genres(genres: List<Genre>?) {
    genres?.map {
        it.name?.let { genre ->
            BulletList(text = genre)
        }
    }
}