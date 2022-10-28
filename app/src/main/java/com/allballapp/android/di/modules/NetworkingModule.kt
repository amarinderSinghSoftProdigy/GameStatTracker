package com.allballapp.android.di.modules

import com.slack.eithernet.ApiResultCallAdapterFactory
import com.slack.eithernet.ApiResultConverterFactory
import com.allballapp.android.BuildConfig.API_SERVER
import com.allballapp.android.BuildConfig.CONNECT_TIMEOUT
import com.allballapp.android.BuildConfig.READ_TIMEOUT
import com.allballapp.android.BuildConfig.WRITE_TIMEOUT
import com.allballapp.android.network.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
        okHttpClient.callTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClient.connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClient.readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClient.writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        okHttpClient.addInterceptor(AuthInterceptor())
        okHttpClient.followSslRedirects(true)
        okHttpClient.build()
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideRestApiService(okHttpClient: OkHttpClient): APIService {
        return Retrofit.Builder()
            .baseUrl(API_SERVER)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(ApiResultCallAdapterFactory)
            .addConverterFactory(ApiResultConverterFactory)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }
}