package com.johan.network

sealed class NetworkResponse<out A, out B> {
    data class Success<out A>(val body: A): NetworkResponse<A, Nothing>()
    data class NetworkError<out B>(val error: Throwable?): NetworkResponse<B, Nothing>()
}