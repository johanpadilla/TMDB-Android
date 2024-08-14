package com.johan.popular_movies.flow

import kotlinx.coroutines.flow.SharingStarted

interface SharingRestartable: SharingStarted {
    fun restart()
}