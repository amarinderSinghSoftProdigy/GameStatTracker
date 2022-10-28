package com.allballapp.android.ui.features.home.events.venues

import com.allballapp.android.data.response.division.VenuesData

data class VenuesUIState(
    val isLoading: Boolean = false,
    val venuesData: List<VenuesData> = emptyList()
)