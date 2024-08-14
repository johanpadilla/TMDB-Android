package com.johan.network.interceptors

import com.johan.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = BuildConfig.ACCESS_TOKEN
        val newRequest = request.newBuilder()
            .addHeader("accept", "application/json")
            .addHeader(
                "Authorization",
                accessToken
            )
            .build()
        return chain.proceed(newRequest)
    }

}