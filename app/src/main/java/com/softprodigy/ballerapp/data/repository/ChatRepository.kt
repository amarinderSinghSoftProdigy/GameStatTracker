package com.softprodigy.ballerapp.data.repository

import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.domain.repository.IChatRepository
import com.softprodigy.ballerapp.network.APIService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val service: APIService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IChatRepository {

}