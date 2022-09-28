package com.softprodigy.ballerapp.ui.features.home.events

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.domain.repository.IEventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    val userRepository: IEventsRepository,
    application: Application
) : AndroidViewModel(application) {


    private val _state = mutableStateOf(EventState())
    var eventState = _state
        private set

    private val _channel = Channel<EventChannel>()
    val eventChannel = _channel.receiveAsFlow()


    fun onEvent(event: EvEvents) {
        when (event) {
            is EvEvents.SetEventId -> {
                _state.value = _state.value.copy(selectedEventId = event.id)
            }
            is EvEvents.GetOpportunityDetail -> {
                viewModelScope.launch {
                    getOpportunityDetail(_state.value.selectedEventId)
                }
            }
            is EvEvents.GetOpportunities -> {
                viewModelScope.launch {
                    getOpportunities()
                }
            }
            is EvEvents.GetFilters -> {
                viewModelScope.launch {
                    getFilters()
                }
            }
            is EvEvents.OnGoingCLick -> {
                _state.value = _state.value.copy(
                    showGoingDialog = true,
                    selectedEvent = event.event
                )
            }
            is EvEvents.OnDeclineCLick -> {
                _state.value = _state.value.copy(
                    showDeclineDialog = true,
                    selectedEvent = event.event
                )
            }
            is EvEvents.onCancel -> {
                _state.value = _state.value.copy(
                    showGoingDialog = false,
                )
            }
            is EvEvents.onCancelDeclineDialog -> {
                _state.value = _state.value.copy(
                    showDeclineDialog = false,
                )
            }
        }

    }


    suspend fun getFilters() {
        _state.value = _state.value.copy(isLoading = true)
        val userResponse = userRepository.getFilters()
        _state.value = _state.value.copy(isLoading = false)

        when (userResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${userResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            userResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                userResponse.value.let { response ->
                    if (response.status) {
                        _state.value =
                            _state.value.copy(filterPreference = response.data.filterPreferences)
                    } else {
                        _channel.send(
                            EventChannel.ShowToast(
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

    suspend fun getOpportunityDetail(eventId: String) {
        _state.value = _state.value.copy(isLoading = true)
        val userResponse = userRepository.getEventOpportunityDetails(eventId)
        _state.value = _state.value.copy(isLoading = false)

        when (userResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${userResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            userResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                userResponse.value.let { response ->
                    if (response.status) {
                        _state.value = _state.value.copy(opportunitiesDetail = response.data)
                    } else {
                        _channel.send(
                            EventChannel.ShowToast(
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

    suspend fun getOpportunities() {
        _state.value = _state.value.copy(isLoading = true)
        val userResponse = userRepository.getEventOpportunities()
        _state.value = _state.value.copy(isLoading = false)

        when (userResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${userResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            userResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                userResponse.value.let { response ->
                    if (response.status) {
                        _state.value = _state.value.copy(opportunitiesList = response.data)
                    } else {
                        _channel.send(
                            EventChannel.ShowToast(
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

sealed class EventChannel {
    data class ShowToast(val message: UiText) : EventChannel()
}
