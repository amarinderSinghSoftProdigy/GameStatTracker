package com.softprodigy.ballerapp.ui.features.home.events

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.domain.repository.IEventRepository
import com.softprodigy.ballerapp.domain.repository.IEventsRepository
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import com.softprodigy.ballerapp.ui.utils.CommonUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    val teamRepo: ITeamRepository,
    val eventRepo: IEventRepository,
    private val userRepository: IEventsRepository,
    application: Application
) : AndroidViewModel(application) {


    private val _state = mutableStateOf(EventState())
    var eventState = _state
        private set

    private val _channel = Channel<EventChannel>()
    val eventChannel = _channel.receiveAsFlow()

    init {
        viewModelScope.launch { getEventList() }
    }

    private suspend fun getEventList() {
        eventState.value =
            eventState.value.copy(showLoading = true)
        val eventResponse = teamRepo.getAllevents()
        eventState.value =
            eventState.value.copy(showLoading = false)

        when (eventResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${eventResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            eventResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                eventResponse.value.let { response ->
                    if (response.status) {
                        _state.value =
                            _state.value.copy(
                                currentEvents = response.data.upcommingEvents,
                                pastEvents = response.data.pastEvents
                            )
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

    suspend fun getEventDetails(id: String) {
        eventState.value =
            eventState.value.copy(showLoading = true)
        val eventResponse = eventRepo.getAllevents()
        eventState.value =
            eventState.value.copy(showLoading = false)

        when (eventResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${eventResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            eventResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                eventResponse.value.let { response ->
                    if (response.status) {
                        _state.value =
                            _state.value.copy(
                                currentEvents = response.data.upcommingEvents,
                                pastEvents = response.data.pastEvents
                            )
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

    fun onEvent(event: EvEvents) {
        when (event) {
            is EvEvents.RegisterPrivacy -> {
                _state.value =
                    _state.value.copy(registerRequest = _state.value.registerRequest.copy(privacy = event.request))
            }
            is EvEvents.RegisterTerms -> {
                _state.value =
                    _state.value.copy(
                        registerRequest = _state.value.registerRequest.copy(
                            termsAndCondition = event.request
                        )
                    )
            }
            is EvEvents.RegisterCash -> {
                _state.value =
                    _state.value.copy(registerRequest = _state.value.registerRequest.copy(payment = event.request))
            }
            is EvEvents.RegisterPlayer -> {
                _state.value =
                    _state.value.copy(registerRequest = _state.value.registerRequest.copy(players = event.request))
            }
            is EvEvents.RegisterDivision -> {
                _state.value =
                    _state.value.copy(
                        divisionData = event.request,
                        registerRequest = _state.value.registerRequest.copy(division = event.request._id)
                    )
            }
            is EvEvents.RegisterNotification -> {
                _state.value =
                    _state.value.copy(
                        registerRequest = _state.value.registerRequest.copy(
                            sendPushNotification = event.request
                        )
                    )
            }
            is EvEvents.RegisterTeam -> {
                _state.value =
                    _state.value.copy(
                        team = event.request,
                        registerRequest = _state.value.registerRequest.copy(team = event.request._id)
                    )
            }
            is EvEvents.UpdateFilters -> {
                viewModelScope.launch {
                    updateFilters(event.request)
                }
            }
            is EvEvents.RegisterForEvent -> {
                viewModelScope.launch {
                    registerForEvent()
                }
            }
            is EvEvents.GetDivisions -> {
                viewModelScope.launch {
                    getEventDivisions(event.id)
                }
            }
            is EvEvents.SetEventId -> {
                _state.value = _state.value.copy(
                    selectedEventId = event.id,
                    registerRequest = _state.value.registerRequest.copy(event = event.id)
                )
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
                    showDeclineDialog = false, declineReason = ""
                )
            }
            is EvEvents.OnSelection -> {
                eventState.value = eventState.value.copy(selectionTeam = event.selected)
            }
            EvEvents.RefreshEventScreen -> {
                viewModelScope.launch { getEventList() }
            }

            EvEvents.OnConfirmGoing -> {
                viewModelScope.launch { acceptEventInvite() }
            }

            is EvEvents.OnGoingDialogClick -> {
                eventState.value = eventState.value.copy(showGoingDialog = event.showGoingDialog)
            }
            is EvEvents.OnDeclineReasonChange -> {
                eventState.value = eventState.value.copy(declineReason = event.reason)
            }

            EvEvents.OnConfirmDeclineClick -> {
                viewModelScope.launch { declineEventInvitation() }
            }
            is EvEvents.ShowToast -> {
                viewModelScope.launch {
                    _channel.send(
                        EventChannel.ShowToast(
                            UiText.DynamicString(
                                event.message

                            )
                        )
                    )
                }
            }
        }

    }


    private suspend fun getFilters() {
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
                            _state.value.copy(
                                filterPreference = response.data.filterPreferences,
                                filters = CommonUtils.getFilterMap(response.data.filterPreferences)
                            )
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

    private suspend fun getEventDivisions(eventId: String) {
        _state.value = _state.value.copy(isLoading = true)
        val userResponse = userRepository.getEventDivisions(eventId)
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
                        _state.value = _state.value.copy(eventDivision = response.data)
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

    private suspend fun updateFilters(request: FilterUpdateRequest) {
        _state.value = _state.value.copy(isLoading = true)
        val userResponse = userRepository.updateFilters(request)
        Timber.e("data " + request.filterPreferences)
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
                        _channel.send(
                            EventChannel.OnSuccess(
                                UiText.DynamicString(
                                    response.statusMessage
                                )
                            )
                        )
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

    private suspend fun registerForEvent() {
        _state.value = _state.value.copy(isLoading = true)
        val userResponse = userRepository.registerForEvent(_state.value.registerRequest)
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
                        _channel.send(
                            EventChannel.OnSuccess(
                                UiText.DynamicString(
                                    response.statusMessage
                                )
                            )
                        )
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


    private suspend fun getOpportunityDetail(eventId: String) {
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

    private suspend fun getOpportunities() {
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

    private suspend fun acceptEventInvite() {
        eventState.value =
            eventState.value.copy(showLoading = true)

        val acceptResponse = eventRepo.acceptEventInvite(eventState.value.selectedEvent.id)

        eventState.value =
            eventState.value.copy(showLoading = false)

        when (acceptResponse) {
            is ResultWrapper.GenericError -> {
                _eventChannel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${acceptResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _eventChannel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            acceptResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                acceptResponse.value.let { response ->

                    if (response.status) {
                        getEventList()
                    } else {
                        _eventChannel.send(
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

    private suspend fun declineEventInvitation() {
        eventState.value =
            eventState.value.copy(showLoading = true)

        val rejectResponse = eventRepo.rejectEventInvite(
            eventState.value.selectedEvent.id,
            eventState.value.declineReason
        )

        eventState.value =
            eventState.value.copy(showLoading = false)

        when (rejectResponse) {
            is ResultWrapper.GenericError -> {
                _eventChannel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${rejectResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _eventChannel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            rejectResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                rejectResponse.value.let { response ->

                    if (response.status) {
                        getEventList()
                    } else {
                        _eventChannel.send(
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
    data class OnSuccess(val message: UiText) : EventChannel()
}
