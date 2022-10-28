package com.allballapp.android.ui.features.venue

sealed class VenueEvent() {
    data class OnSearchVenueChange(val venue: String) : VenueEvent()
}
