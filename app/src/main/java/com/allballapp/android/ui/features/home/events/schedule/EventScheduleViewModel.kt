package com.allballapp.android.ui.features.home.events.schedule

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.core.util.UiText
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.domain.repository.IEventsRepository
import com.allballapp.android.ui.features.home.events.EvEvents
import com.allballapp.android.ui.features.home.events.EventChannel
import com.allballapp.android.ui.features.home.events.EventState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventScheduleViewModel @Inject constructor(
    dataStoreManager: DataStoreManager,
    private val eventsRepo: IEventsRepository,
) : ViewModel() {
    var eventScheduleState = mutableStateOf(EventScheduleState())
        private set

    private val _state = mutableStateOf(EventState())
    var eventState: State<EventState> = _state

    private val _channel = Channel<EventChannel>()
    val eventChannel = _channel.receiveAsFlow()

    init {
        /*eventScheduleState.value = eventScheduleState.value.copy(
            leagueSchedules = listOf(
                LeagueScheduleModel(
                    "Sep 1, 2022", "2", matches = listOf(
                        Match(
                            teamA = Team(
                                name = "Titans",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            teamB = Team(
                                name = "Champions",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            location = "Chicago,IL",
                            divisions = "Boys 8th",
                            time = "5:00 PM"
                        ), Match(
                            teamA = Team(
                                name = "Royals",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            teamB = Team(
                                name = "Lions",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            location = "Chicago,IL",
                            divisions = "Boys 8th",
                            time = "5:00 PM"
                        )
                    )
                ),
                LeagueScheduleModel(
                    "Sep 3, 2022", "3", matches = listOf(
                        Match(
                            teamA = Team(
                                name = "Titans",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            teamB = Team(
                                name = "Champions",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            location = "Chicago,IL",
                            divisions = "Boys 8th",
                            time = "5:00 PM"
                        ), Match(
                            teamA = Team(
                                name = "Royals",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            teamB = Team(
                                name = "Lions",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            location = "Chicago,IL",
                            divisions = "Boys 8th",
                            time = "5:00 PM"
                        ), Match(
                            teamA = Team(
                                name = "Kings",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            teamB = Team(
                                name = "Warriors",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            location = "Chicago,IL",
                            divisions = "Boys 8th",
                            time = "5:00 PM"
                        )
                    )
                ),
                LeagueScheduleModel(
                    "Sep 4, 2022", "2", matches = listOf(
                        Match(
                            teamA = Team(
                                name = "Titans",
                                logo = "teamLogo/1662462524256-IMG_20220805_120020_710.jpg"
                            ),
                            teamB = Team(
                                name = "Champions",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            location = "Chicago,IL",
                            divisions = "Boys 8th",
                            time = "5:00 PM"
                        ), Match(
                            teamA = Team(
                                name = "Royals",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            teamB = Team(
                                name = "Lions",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            location = "Chicago,IL",
                            divisions = "Boys 8th",
                            time = "5:00 PM"
                        )
                    )
                )
            )
        )*/
    }


    private suspend fun getEventSchedule(eventId: String) {
        _state.value =
            eventState.value.copy(showLoading = true)
        val eventResponse = eventsRepo.getEventScheduleDetails(eventId)
        _state.value =
            eventState.value.copy(showLoading = false)

        when (eventResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    EventChannel.ShowEventDetailsToast(
                        UiText.DynamicString(
                            "${eventResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
              /*  _channel.send(
                    EventChannel.ShowEventDetailsToast(
                        UiText.DynamicString(
                            eventResponse.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                eventResponse.value.let { response ->
                    if (response.status) {

                    } else {
                        _channel.send(
                            EventChannel.ShowEventDetailsToast(
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
            is EvEvents.ClearRegister -> {
            }
            is EvEvents.GetSchedule -> {
                viewModelScope.launch {
                   // getEventDetails(event.eventId)
                }

            }
        }
    }
}