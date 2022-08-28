package com.softprodigy.ballerapp.di.modules

import com.softprodigy.ballerapp.data.UserStorage
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        if (!UserStorage.token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer ${UserStorage.token}")
        }

        return chain.proceed(requestBuilder.build())
    }
}