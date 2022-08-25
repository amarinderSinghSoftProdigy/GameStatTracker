package com.softprodigy.ballerapp.di.modules
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.repository.UserRepository
import com.softprodigy.ballerapp.domain.repository.IUserRepository
import com.softprodigy.ballerapp.network.APIService
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
}
