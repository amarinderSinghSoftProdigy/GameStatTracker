package com.allballapp.android.di.modules
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.data.repository.*
import com.allballapp.android.domain.repository.*
import com.allballapp.android.network.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepo(
        apiService: APIService,
        dataStoreManager: DataStoreManager,
    ): IUserRepository {
        return UserRepository(service = apiService, dataStoreManager = dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideTeamRepo(
        apiService: APIService,
        dataStoreManager: DataStoreManager,
    ): ITeamRepository {
        return TeamRepository(service = apiService, dataStoreManager = dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideChatRepo(
        apiService: APIService,
        dataStoreManager: DataStoreManager,
    ): IChatRepository {
        return ChatRepository(service = apiService, dataStoreManager = dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideEventsRepo(
        apiService: APIService,
        dataStoreManager: DataStoreManager,
    ): IEventsRepository {
        return EventsRepository(service = apiService, dataStoreManager = dataStoreManager)
    }
    @Provides
    @Singleton
    fun provideImageUploadRepo(
        apiService: APIService,
        dataStoreManager: DataStoreManager,
    ): IImageUploadRepo {
        return ImageUploadRepo(service = apiService, dataStoreManager = dataStoreManager)
    }
}
