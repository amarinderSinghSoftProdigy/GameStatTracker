package com.softprodigy.ballerapp.ui.features.home.events.new_event

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.request.CreateEventReq
import com.softprodigy.ballerapp.domain.repository.IEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewEventViewModel @Inject constructor(val eventRepository: IEventRepository) : ViewModel() {
    private val _state = mutableStateOf(NewEventState())
    val state: State<NewEventState> = _state

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

            is NewEvEvent.OnLocationChanged -> {
                _state.value =
                    _state.value.copy(selectedLocation = event.location)

            }

            is NewEvEvent.OnAddressChanged -> {
                _state.value =
                    _state.value.copy(selectedAddress = event.address)
            }
            NewEvEvent.OnSaveButtonClick -> {

            }
        }
    }

    private suspend fun createEvent() {
        _state.value =
            _state.value.copy(isLoading = true)

        val request = CreateEventReq(
            eventName = state.value.eventName,
            eventType = state.value.eventType,
            myTeam = UserStorage.teamId,
            date = state.value.selectedDate,
            arrivalTime = state.value.selectedArrivalTime,
            startTime = state.value.selectedStartTime,

        )
//        eventRepository.createEvent()
    }
}