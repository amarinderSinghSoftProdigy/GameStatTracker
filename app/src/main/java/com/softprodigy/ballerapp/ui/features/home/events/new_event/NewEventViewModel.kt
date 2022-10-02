package com.softprodigy.ballerapp.ui.features.home.events.new_event

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.request.CreateEventReq
import com.softprodigy.ballerapp.data.request.Location
import com.softprodigy.ballerapp.domain.repository.IEventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewEventViewModel @Inject constructor(val eventsRepository: IEventsRepository) : ViewModel() {
    private val _state = mutableStateOf(NewEventState())
    val state: State<NewEventState> = _state

    private val _channel = Channel<NewEventChannel>()
    val channel = _channel.receiveAsFlow()


    fun onEvent(event: NewEvEvent) {
        when (event) {

            is NewEvEvent.OnEventNameChange -> {
                _state.value = _state.value.copy(eventName = event.eventName)

            }

            is NewEvEvent.OnEventTypeChange -> {
                _state.value = _state.value.copy(eventType = event.eventType)
            }

            is NewEvEvent.OnDateChanged -> {
                _state.value = _state.value.copy(selectedDate = event.date)
            }

            is NewEvEvent.OnArrivalTimeChanged -> {
                _state.value =
                    _state.value.copy(selectedArrivalTime = event.time)

            }

            is NewEvEvent.OnStartTimeChanged -> {
                _state.value = _state.value.copy(selectedStartTime = event.time)

            }

            is NewEvEvent.OnEndTimeChanged -> {
                _state.value = _state.value.copy(selectedEndTime = event.time)

            }

            is NewEvEvent.OnLocationVenueChange -> {
                _state.value =
                    _state.value.copy(selectedVenueName = event.venueName)
            }

            is NewEvEvent.OnAddressChanged -> {
                _state.value =
                    _state.value.copy(selectedAddress = event.address)
            }
            NewEvEvent.OnSaveButtonClick -> {
                viewModelScope.launch {
                    createEvent()
                }
            }
            is NewEvEvent.OnNotificationChange -> {
                _state.value =
                    _state.value.copy(showNotification = event.showNotification)
            }
        }
    }

    private suspend fun createEvent() {
        _state.value =
            _state.value.copy(isLoading = true)


        val location = Location(
            type = "Point",
            coordinates = arrayListOf(
                state.value.selectedAddress.lat,
                state.value.selectedAddress.long
            )
        )

        val request = CreateEventReq(
            eventName = state.value.eventName,
            eventType = state.value.eventType,
            myTeam = UserStorage.teamId,
            date = state.value.selectedDate,
            arrivalTime = state.value.selectedArrivalTime,
            startTime = state.value.selectedStartTime,
            endTime = state.value.selectedEndTime,
            landmarkLocation = state.value.selectedVenueName,
            location = location,
            address = state.value.selectedAddress,
            pushNotification = state.value.showNotification

        )
        val eventResponse = eventsRepository.createEvent(request)
        _state.value =
            _state.value.copy(isLoading = false)

        when (eventResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    NewEventChannel.ShowToast(
                        UiText.DynamicString(
                            "${eventResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _channel.send(
                    NewEventChannel.ShowToast(
                        UiText.DynamicString(
                            eventResponse.message
                        )
                    )
                )

            }
            is ResultWrapper.Success -> {
                eventResponse.value.let { response ->
                    if (response.status) {
                        _channel.send(
                            NewEventChannel.OnEventCreationSuccess(
                                response.statusMessage
                            )
                        )
                    } else {
                        _channel.send(
                            NewEventChannel.ShowToast(
                                UiText.DynamicString(
                                    response.statusMessage
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

sealed class NewEventChannel() {
    data class ShowToast(val message: UiText) : NewEventChannel()
    data class OnEventCreationSuccess(val message: String) : NewEventChannel()

}