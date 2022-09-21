package com.softprodigy.ballerapp.ui.features.home.events

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import com.softprodigy.ballerapp.ui.theme.GreenColor
import com.softprodigy.ballerapp.ui.theme.Yellow700
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(val teamRepo: ITeamRepository) : ViewModel() {
    var eventState = mutableStateOf(EventState())
        private set

    private val _invitationChannel = Channel<InvitationChannel>()
    val invitationChannel = _invitationChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            eventState.value =
                eventState.value.copy(
                    currentEvents = arrayListOf(
                        Events(
                            "1",
                            "Practice Title",
                            "Venue Name",
                            "6:00 PM - 7:00 PM",
                            EventStatus.PENDING.status,
                            EventType.PRACTICE
                        ),
                        Events(
                            "2",
                            "Game Title",
                            "Venue Name1",
                            "6:00 PM - 7:00 PM",
                            EventStatus.ACCEPT.status,
                            EventType.GAME,
                        ),
                    ),
                    pastEvents = arrayListOf(
                        Events(
                            "1",
                            "Practice Title",
                            "Venue Name",
                            "6:00 PM - 7:00 PM",
                            EventStatus.PAST.status,
                            EventType.PRACTICE,
                        ),
                        Events(
                            "2",
                            "Practice Title1",
                            "Venue Name1",
                            "6:00 PM - 7:00 PM",
                            EventStatus.PAST.status,
                            EventType.ACTIVITY
                        ),
                        Events(
                            "3",
                            "Practice Title2",
                            "Venue Name2",
                            "6:00 PM - 7:00 PM",
                            EventStatus.PAST.status,
                            EventType.SCRIMMAGE
                        ),
                    ),
                    leagues = arrayListOf(
                        Leagues(
                            "1",
                            "League Title",
                            "1389 Aviator Ave, Eagle Mountain",
                            "",
                            "Sep 1 - Dec 15, 2022",
                            "League",
                            Yellow700

                        ),
                        Leagues(
                            "2",
                            "Tournament Title",
                            "1389 Aviator Ave, Eagle Mountain",
                            "",
                            "Sep 1 - Dec 15, 2022",
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
                            "Sep 1 - Dec 15, 2022",
                            "League",
                            Yellow700
                        ),
                        Leagues(
                            "2",
                            "Tournament Title",
                            "1389 Aviator Ave, Eagle Mountain",
                            "",
                            "Sep 1 - Dec 15, 2022",
                            "Tournament",
                            GreenColor
                        ),
                    )
                )

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
                    showDeclineDialog = false,
                )
            }
        }

    }

}

sealed class InvitationChannel {
    data class ShowToast(val message: UiText) : InvitationChannel()
}
