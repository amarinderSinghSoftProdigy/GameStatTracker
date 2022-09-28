package com.softprodigy.ballerapp.ui.features.venue

sealed class VenueEvent() {
    data class OnSearchVenueChange(val venue: String) : VenueEvent()
}
