package com.softprodigy.ballerapp.ui.features.home.events

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.domain.repository.IEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(val eventRepo: IEventRepository) : ViewModel() {
    var eventState = mutableStateOf(EventState())
        private set

    private val _eventChannel = Channel<EventChannel>()
    val eventChannel = _eventChannel.receiveAsFlow()

    init {
     /*   viewModelScope.launch {
            getEventList()
            eventState.value =
                eventState.value.copy(
                    currentEvents = arrayListOf(
                        Events(
                            "1",
                            "Practice Title",
                            "Venue Name",
                            "Fri, May 20 6:00 PM - 7:00 PM",
                            EventStatus.PENDING.status,
                            "practice"
                        ),
                        Events(
                            "2",
                            "Game Title",
                            "Venue Name1",
                            "Fri, May 20 6:00 PM - 7:00 PM",
                            EventStatus.ACCEPT.status,
                            "game",
                        ),
                    ),
                    pastEvents = arrayListOf(
                        Events(
                            "1",
                            "Practice Title",
                            "Venue Name",
                            "Fri, May 20 6:00 PM - 7:00 PM",
                            EventStatus.PAST.status,
                            "practice",
                        ),
                        Events(
                            "2",
                            "Practice Title1",
                            "Venue Name1",
                            "Fri, May 20 6:00 PM - 7:00 PM",
                            EventStatus.PAST.status,
                            "activity"
                        ),
                        Events(
                            "3",
                            "Practice Title2",
                            "Venue Name2",
                            "Fri, May 20 6:00 PM - 7:00 PM",
                            EventStatus.PAST.status,
                            "Scrimmage"
                        ),
                    ),
                    leagues = arrayListOf(
                        Leagues(
                            "1",
                            "League Title",
                            "1389 Aviator Ave, Eagle Mountain",
                            "",
                            "Fri, May 20 Sep 1 - Dec 15, 2022",
                            "League",
                            ColorPrimaryOrange

                        ),
                        Leagues(
                            "2",
                            "Tournament Title",
                            "1389 Aviator Ave, Eagle Mountain",
                            "",
                            "Fri, May 20 Sep 1 - Dec 15, 2022",
                            "Tournament",
                            GreenColor
                        ),
                    ),
                    oppotuntities = arrayListOf(
                        Leagues(
                            "1",
                            "League Title",
                            "1389 Aviator Ave, Eagle Mountain",
                            "",
                            "Fri, May 20 Sep 1 - Dec 15, 2022",
                            "League",
                            Yellow700
                        ),
                        Leagues(
                            "2",
                            "Tournament Title",
                            "1389 Aviator Ave, Eagle Mountain",
                            "",
                            "Fri, May 20 Sep 1 - Dec 15, 2022",
                            "Tournament",
                            GreenColor
                        ),
                    )
                )
        }*/
    }

    private suspend fun getEventList() {
        eventState.value =
            eventState.value.copy(showLoading = true)
        val eventResponse = eventRepo.getAllevents()
        eventState.value =
            eventState.value.copy(showLoading = false)

        when (eventResponse) {
            is ResultWrapper.GenericError -> {
                _eventChannel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${eventResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _eventChannel.send(
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
                        eventState.value =
                            eventState.value.copy(
                                    currentEvents = response.data.upcommingEvents,
                                    pastEvents = response.data.pastEvents
                                )
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

    suspend fun getEventDetails(id :String){
        eventState.value =
            eventState.value.copy(showLoading = true)
        val eventResponse = eventRepo.getAllevents()
        eventState.value =
            eventState.value.copy(showLoading = false)

        when (eventResponse) {
            is ResultWrapper.GenericError -> {
                _eventChannel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${eventResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _eventChannel.send(
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
                        eventState.value =
                            eventState.value.copy(
                                currentEvents = response.data.upcommingEvents,
                                pastEvents = response.data.pastEvents
                            )
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

    fun onEvent(event: EvEvents) {
        when (event) {
            is EvEvents.OnGoingCLick -> {
                eventState.value = eventState.value.copy(
                    showGoingDialog = true,
                    selectedEvent = event.event
                )

            }
            is EvEvents.OnDeclineCLick -> {
                eventState.value = eventState.value.copy(
                    showDeclineDialog = true,
                    selectedEvent = event.event
                )
            }
            is EvEvents.onCancel -> {
                eventState.value = eventState.value.copy(
                    showGoingDialog = false,
                )
            }
            is EvEvents.onCancelDeclineDialog -> {
                eventState.value = eventState.value.copy(
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
               eventState.value=eventState.value.copy(showGoingDialog = event.showGoingDialog)
            }
            is EvEvents.OnDeclineReasonChange -> {
                eventState.value=eventState.value.copy(declineReason = event.reason)
            }

            EvEvents.OnConfirmDeclineClick ->{
                viewModelScope.launch { declineEventInvitation() }
            }
//        }

            /* is EvEvents.OnReasonSelection -> {
                 eventState.value = eventState.value.copy(reasonTeam = event.text)
             }*/
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

        when(rejectResponse){
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
}
