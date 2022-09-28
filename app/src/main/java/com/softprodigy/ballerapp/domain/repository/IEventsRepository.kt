package com.softprodigy.ballerapp.domain.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.domain.BaseResponse
import com.softprodigy.ballerapp.ui.features.home.events.FilterResponse
import com.softprodigy.ballerapp.ui.features.home.events.OpportunitiesDetail
import com.softprodigy.ballerapp.ui.features.home.events.OpportunitiesItem
import javax.inject.Singleton

@Singleton
interface IEventsRepository {
    suspend fun getFilters(): ResultWrapper<BaseResponse<FilterResponse>>
    suspend fun getEventOpportunities(): ResultWrapper<BaseResponse<List<OpportunitiesItem>>>
    suspend fun getEventOpportunityDetails(id: String): ResultWrapper<BaseResponse<OpportunitiesDetail>>
}