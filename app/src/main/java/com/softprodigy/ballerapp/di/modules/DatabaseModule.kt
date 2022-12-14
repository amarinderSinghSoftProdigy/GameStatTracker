package com.softprodigy.ballerapp.di.modules

import android.content.Context
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun dataStore(@ApplicationContext appContext: Context) =
        DataStoreManager(appContext = appContext)
}