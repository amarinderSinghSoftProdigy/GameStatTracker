package com.softprodigy.ballerapp.di.modules

import com.softprodigy.ballerapp.data.UserStorage
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        if (!UserStorage.token.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer ${UserStorage.token}")
            Timber.e("Token ${UserStorage.token}")
        }

        return chain.proceed(requestBuilder.build())
    }
}