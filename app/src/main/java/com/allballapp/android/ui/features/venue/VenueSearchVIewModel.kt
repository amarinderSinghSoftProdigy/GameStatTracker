package com.allballapp.android.ui.features.venue

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.core.util.UiText
import com.allballapp.android.domain.repository.ITeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueSearchVIewModel @Inject constructor(val teamRepository: ITeamRepository) : ViewModel() {

    private val _state = mutableStateOf(VenueState())
    val state: State<VenueState> = _state

    private val _channel = Channel<VenueChannel>()
    val channel = _channel.receiveAsFlow()

    fun onEvent(event: VenueEvent) {
        when (event) {
            is VenueEvent.OnSearchVenueChange -> {
                _state.value = _state.value.copy(searchVenue = event.venue)
                viewModelScope.launch {
                    if (_state.value.searchVenue.isNotEmpty()) {
                        _state.value = _state.value.copy(venues = emptyList())
                        getAllVenue(event.venue)
                    }
                    if (_state.value.searchVenue.isEmpty()) {
                        _state.value = _state.value.copy(venues = emptyList())
                    }
                }
            }
        }
    }

    private suspend fun getAllVenue(venue: String) {
        _state.value = _state.value.copy(isLoading = true)
        val venueResponse = teamRepository.getAllVenue(search = venue)
        _state.value = _state.value.copy(isLoading = false)

        when (venueResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    VenueChannel.ShowToast(
                        UiText.DynamicString(
                            "${venueResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _channel.send(
                    VenueChannel.ShowToast(
                        UiText.DynamicString(
                            venueResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                venueResponse.value.let { response ->
                    if (response.data != null) {
                        _state.value = _state.value.copy(venues = response.data)
                    }
                }
            }
        }
    }
}

sealed class VenueChannel() {
    data class ShowToast(val message: UiText) : VenueChannel()

}