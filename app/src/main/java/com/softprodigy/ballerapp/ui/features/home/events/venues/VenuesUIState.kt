package com.softprodigy.ballerapp.ui.features.home.events.venues

import com.softprodigy.ballerapp.data.response.division.VenuesData

data class VenuesUIState(
    val isLoading: Boolean = false,
    val venuesData: List<VenuesData> = emptyList()
)