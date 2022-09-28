package com.softprodigy.ballerapp.data.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.safeApiCall
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.domain.BaseResponse
import com.softprodigy.ballerapp.domain.repository.IEventsRepository
import com.softprodigy.ballerapp.network.APIService
import com.softprodigy.ballerapp.ui.features.home.events.FilterResponse
import com.softprodigy.ballerapp.ui.features.home.events.OpportunitiesDetail
import com.softprodigy.ballerapp.ui.features.home.events.OpportunitiesItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val service: APIService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IEventsRepository {
    override suspend fun getFilters(): ResultWrapper<BaseResponse<FilterResponse>> {
        return safeApiCall(dispatcher) {
            service.getFilters()
        }
    }

    override suspend fun getEventOpportunities(): ResultWrapper<BaseResponse<List<OpportunitiesItem>>> {
        return safeApiCall(dispatcher) {
            service.getAllOpportunities()
        }
    }

    override suspend fun getEventOpportunityDetails(id: String): ResultWrapper<BaseResponse<OpportunitiesDetail>> {
        return safeApiCall(dispatcher) {
            service.getOpportunityDetail(id)
        }
    }


}