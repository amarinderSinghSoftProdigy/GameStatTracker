package com.allballapp.android.ui.features.venue

data class VenueState(
    val isLoading: Boolean = false,
    val searchVenue: String = "",
    val venues: List<VenueDetails> = listOf()
)