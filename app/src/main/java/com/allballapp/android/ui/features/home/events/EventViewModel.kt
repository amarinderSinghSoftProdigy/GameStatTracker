package com.allballapp.android.ui.features.home.events

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.allballapp.android.common.AppConstants
import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.common.convertStringDateToLong
import com.allballapp.android.data.UserStorage
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.data.response.Format
import com.allballapp.android.data.response.GenderList
import com.allballapp.android.domain.repository.IEventsRepository
import com.allballapp.android.domain.repository.ITeamRepository
import com.allballapp.android.ui.features.venue.VenueDetails
import com.allballapp.android.ui.utils.CommonUtils
import com.allballapp.android.ui.utils.UiText
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
    private val eventsRepo: IEventsRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _state = mutableStateOf(EventState())
    var eventState: State<EventState> = _state

    private val _channel = Channel<EventChannel>()
    val eventChannel = _channel.receiveAsFlow()

    private suspend fun getEventList() {
        _state.value =
            eventState.value.copy(showLoading = true)
        val eventResponse = eventsRepo.getAllevents(UserStorage.teamId)
        _state.value =
            eventState.value.copy(showLoading = false)

        when (eventResponse) {
            is ResultWrapper.GenericError -> {
                /* _channel.send(
                     EventChannel.ShowToast(
                         UiText.DynamicString(
                             "${eventResponse.message}"
                         )
                     )
                 )*/
            }
            is ResultWrapper.NetworkError -> {
                /* _channel.send(
                     EventChannel.ShowToast(
                         UiText.DynamicString(
                             eventResponse.message
                         )
                     )
                 )*/
            }
            is ResultWrapper.Success -> {
                eventResponse.value.let { response ->
                    if (response.status && (response.data.upcommingEvents.isNotEmpty())
                        || (response.data.upcommingEvents.isNotEmpty())
                        || (response.data.publishedGames.isNotEmpty())
                    ) {
                        val upcomingAndGameData =
                            response.data.upcommingEvents + response.data.publishedGames

                        val sortedUpcomingAndGameData = upcomingAndGameData.sortedByDescending {
                            when (it) {
                                is Events -> {
                                    convertStringDateToLong(it.date)
                                }
                                is PublishedGames -> {
                                    convertStringDateToLong(it.date)
                                }
                                else -> {
                                    0
                                }
                            }
                        }
                        Timber.i("sortedUpcomingAndGameData--$sortedUpcomingAndGameData")

                        _state.value =
                            _state.value.copy(
                                currentEvents = response.data.upcommingEvents,
                                pastEvents = response.data.pastEvents,
                                upcomingAndGameData = sortedUpcomingAndGameData
                            )
                    } else {
                        /* _channel.send(
                             EventChannel.ShowToast(
                                 UiText.DynamicString(
                                     response.statusMessage
                                 )
                             )
                         )*/
                    }
                }
            }
        }
    }

    suspend fun getEventDetails(eventId: String) {
        _state.value =
            eventState.value.copy(showLoading = true)
        val eventResponse = eventsRepo.getEventDetails(eventId)
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
                /* _channel.send(
                     EventChannel.ShowEventDetailsToast(
                         UiText.DynamicString(
                             eventResponse.message
                         )
                     )
                 )*/
            }
            is ResultWrapper.Success -> {
                eventResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _state.value =
                            _state.value.copy(
                                event = response.data
                            )
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
            is EvEvents.ClearListEvents -> {
                _state.value =
                    _state.value.copy(
                        currentEvents = arrayListOf(),
                        pastEvents = arrayListOf(),
                        upcomingAndGameData = arrayListOf()
                    )
            }
            is EvEvents.ClearList -> {
                _state.value = _state.value.copy(opportunitiesList = emptyList())
            }
            is EvEvents.ClearListLeague -> {
                _state.value = _state.value.copy(myLeaguesList = arrayListOf())
            }
            is EvEvents.ClearRegister -> {
                _state.value =
                    _state.value.copy(registerRequest = RegisterRequest())
            }
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
                _state.value.registerRequest.players.clear()
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
            is EvEvents.RegisterGameStaff -> {
                viewModelScope.launch {
                    registerGameStaffEvent()
                }
            }
            is EvEvents.GetDivisions -> {
                viewModelScope.launch {
                    getEventDivisions(event.id)
                    _state.value = _state.value.copy(
                        registerRequest = _state.value.registerRequest.copy(event = event.id)
                    )
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
                    getOpportunities(event.type)
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
                _state.value = _state.value.copy(selectionTeam = event.selected)
            }
            EvEvents.RefreshEventScreen -> {
                viewModelScope.launch { getEventList() }
            }

            is EvEvents.RefreshEventDetailsScreen -> {
                viewModelScope.launch { getEventDetails(event.eventId) }
            }

            is EvEvents.OnConfirmGoing -> {
                viewModelScope.launch { acceptEventInvite() }
            }

            is EvEvents.OnGoingDialogClick -> {
                _state.value = _state.value.copy(showGoingDialog = event.showGoingDialog)
            }
            is EvEvents.OnDeclineReasonChange -> {
                _state.value = _state.value.copy(declineReason = event.reason)
            }

            is EvEvents.OnConfirmDeclineClick -> {
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
            is EvEvents.GetMyLeagues -> {
                viewModelScope.launch {
                    getMyLeagues(if (event.type != AppConstants.MY_LEAGUE) event.type else "")
                }
            }
            is EvEvents.GetLeagueId -> {
                _state.value = _state.value.copy(eventId = event.eventId)
            }

            is EvEvents.GetGender -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(gender = event.gender)
                    getDivisions()
                }
            }
            is EvEvents.PostNoteTimeSpan -> {
                _state.value =
                    eventState.value.copy(isPostPracticeTimeSpan = event.showPostNoteButton)
            }
            is EvEvents.PreNoteTimeSpan -> {
                _state.value =
                    eventState.value.copy(isPrePracticeTimeSpan = event.showPreNoteButton)

            }

            is EvEvents.GetDivision -> {
                viewModelScope.launch {
                    getDivisions()
                }
            }
            is EvEvents.ShowPrePostPracticeAddNoteDialog -> {
                _state.value = eventState.value.copy(
                    showPrePostNoteDialog = event.showPostNoteDialog,
                    noteType = event.noteType,
                    note = ""
                )

            }
            is EvEvents.OnNoteChange -> {
                _state.value = eventState.value.copy(note = event.note)
            }
            is EvEvents.OnAddNoteConfirmClick -> {
                viewModelScope.launch {
                    addNote(event.noteType, event.note, event.eventId)
                }
            }
            is EvEvents.GetVenues -> {
                viewModelScope.launch {
                    getVenues()
                }
            }

            is EvEvents.RefreshTeamsByLeagueAndDivision -> {
                _state.value = _state.value.copy(teamsByLeagueDivision = emptyList())
                viewModelScope.launch { getTeamsByLeagueAndDivision(divisionId = event.divisionId) }

            }

            is EvEvents.RefreshTeamsByDivision -> {
                viewModelScope.launch { getTeamsByLeagueIdAllDivision(_state.value.eventId) }
            }
            is EvEvents.RefreshVenueDetailsById -> {
                _state.value = _state.value.copy(venueDetails = VenueDetails())
                viewModelScope.launch { getVenueDetailsById(event.venueId) }
            }

            is EvEvents.RefreshStandingByLeagueDivision -> {
                viewModelScope.launch {
                    getAllTeamsStandingByLeaguedAndDivision(
                        eventId = _state.value.eventId,
                        divisionId = event.divisionId
                    )
                }
            }
            is EvEvents.OnLeagueDivisionStandingSelected -> {
                _state.value = _state.value.copy(
                    standingUIState = _state.value.standingUIState.copy(
                        selectedStanding = event.standing
                    )
                )
            }
            is EvEvents.GetRefereeFilters -> {
                getRefereeFilters()
            }

            is EvEvents.GenderSelected -> {
                _state.value = _state.value.copy(selectedGender = event.gender)
            }

            is EvEvents.EventType -> {
                _state.value = _state.value.copy(selectedEventType = event.eventType)

            }

            is EvEvents.Format -> {
                _state.value = _state.value.copy(selectedFormat = event.format)
            }

            is EvEvents.GetSchedule -> {
                viewModelScope.launch {
                    getEventSchedule(event.eventId)
                }
            }
            is EvEvents.OnRoleClick -> {
                _state.value = _state.value.copy(selectedRole = event.role)
            }

            is EvEvents.GetRoles -> {
                viewModelScope.launch {
                    getUserRoles()
                }
            }

            is EvEvents.ClearRequest -> {
                _state.value = _state.value.copy(registerRequest = RegisterRequest())
            }

            is EvEvents.ClearTeam -> {
                _state.value = _state.value.copy(teamsByLeagueDivision = emptyList())
            }
            is EvEvents.ClearOpportunities -> {
                _state.value = _state.value.copy(opportunitiesDetail = OpportunitiesDetail())
            }

            is EvEvents.PaymentOption -> {
                if (event.paymentOption != _state.value.registerRequest.paymentOption) {
                    _state.value = _state.value.copy(
                        registerRequest = _state.value.registerRequest.copy(
                            paymentOption = event.paymentOption,
                            payment = ""
                        )
                    )
                }
            }
            else -> {}
        }
    }

    private suspend fun addNote(noteType: NoteType, note: String, eventId: String) {

        _state.value =
            eventState.value.copy(showLoading = true)
        val addNoteResponse =
            eventsRepo.addPrePostNote(note = note, noteType = noteType, eventId = eventId)
        _state.value =
            eventState.value.copy(showLoading = false)

        when (addNoteResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    EventChannel.ShowEventDetailsToast(
                        UiText.DynamicString(
                            "${addNoteResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                /* _channel.send(
                     EventChannel.ShowEventDetailsToast(
                         UiText.DynamicString(
                             addNoteResponse.message
                         )
                     )
                 )*/

            }
            is ResultWrapper.Success -> {
                addNoteResponse.value.let { response ->

                    if (response.status) {
                        _channel.send(
                            EventChannel.OnUpdateNoteSuccess(
                                response.statusMessage
                            )
                        )
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

    private suspend fun getFilters() {
        _state.value = _state.value.copy(isLoading = true)
        val userResponse = eventsRepo.getFilters()
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
                /*_channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            userResponse.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                userResponse.value.let { response ->
                    if (response.status && response.data != null) {
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
        val userResponse = eventsRepo.getEventDivisions(eventId)
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
                /*_channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            userResponse.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                userResponse.value.let { response ->
                    if (response.status && response.data != null) {
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
        val userResponse = eventsRepo.updateFilters(request)
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
                /*_channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            userResponse.message
                        )
                    )
                )*/
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
        val userResponse = eventsRepo.registerForEvent(_state.value.registerRequest)
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
                /* _channel.send(
                     EventChannel.ShowToast(
                         UiText.DynamicString(
                             userResponse.message
                         )
                     )
                 )*/
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
                        _state.value = _state.value.copy(registerRequest = RegisterRequest())

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

    private suspend fun registerGameStaffEvent() {
        _state.value = _state.value.copy(isLoading = true)

        _state.value = _state.value.copy(
            registerGameStaff = _state.value.registerGameStaff.copy(
                eventId = _state.value.opportunitiesDetail.id,
                role = _state.value.selectedRole,
                location = _state.value.location
            )
        )

        val userResponse = eventsRepo.registerGameStaff(_state.value.registerGameStaff)
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
                /*  _channel.send(
                      EventChannel.ShowToast(
                          UiText.DynamicString(
                              userResponse.message
                          )
                      )
                  )*/
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
        val userResponse = eventsRepo.getEventOpportunityDetails(eventId)
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
                /* _channel.send(
                     EventChannel.ShowToast(
                         UiText.DynamicString(
                             userResponse.message
                         )
                     )
                 )*/
            }
            is ResultWrapper.Success -> {
                userResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _state.value = _state.value.copy(
                            opportunitiesDetail = response.data,
                            price = response.data.standardPrice,
                            location = response.data.location
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

    private suspend fun getOpportunities(type: String) {
        _state.value = _state.value.copy(isLoading = true)
        val userResponse = eventsRepo.getEventOpportunities(type = type, UserStorage.teamId)
        _state.value = _state.value.copy(isLoading = false)

        when (userResponse) {
            is ResultWrapper.GenericError -> {
                //_state.value = _state.value.copy(opportunitiesList = mutableListOf())
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${userResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                // _state.value = _state.value.copy(opportunitiesList = mutableListOf())
                /*  _channel.send(
              EventChannel.ShowToast(
                  UiText.DynamicString(
                      userResponse.message
                  )
              )
          )*/
            }
            is ResultWrapper.Success -> {
                userResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _state.value = _state.value.copy(opportunitiesList = response.data)
                    } else {
                        _state.value = _state.value.copy(opportunitiesList = mutableListOf())
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
        _state.value =
            _state.value.copy(showLoading = true)

        val acceptResponse = eventsRepo.acceptEventInvite(eventState.value.selectedEvent.id)

        _state.value =
            _state.value.copy(showLoading = false)

        when (acceptResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${acceptResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                /* _channel.send(
                     EventChannel.ShowToast(
                         UiText.DynamicString(
                             acceptResponse.message
                         )
                     )
                 )*/
            }
            is ResultWrapper.Success -> {
                acceptResponse.value.let { response ->

                    if (response.status) {
                        getEventList()
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

    private suspend fun declineEventInvitation() {
        _state.value =
            eventState.value.copy(showLoading = true)

        val rejectResponse = eventsRepo.rejectEventInvite(
            eventState.value.selectedEvent.id,
            eventState.value.declineReason
        )

        _state.value =
            eventState.value.copy(showLoading = false)

        when (rejectResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${rejectResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                /*   _channel.send(
                       EventChannel.ShowToast(
                           UiText.DynamicString(
                               rejectResponse.message
                           )
                       )
                   )*/
            }
            is ResultWrapper.Success -> {
                rejectResponse.value.let { response ->
                    if (response.status) {
                        getEventList()
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

    private suspend fun getMyLeagues(type: String) {

        _state.value = _state.value.copy(isLoading = true)
        val userResponse = teamRepo.getMyLeagues(type = type, UserStorage.teamId)
        _state.value = _state.value.copy(isLoading = false)


        when (userResponse) {
            is ResultWrapper.GenericError -> {
                //_state.value = _state.value.copy(isLoading = false,myLeaguesList = arrayListOf())
                /* _state.value = _state.value.copy(isLoading = false,myLeaguesList = arrayListOf())*/
                /* _channel.send(
                     EventChannel.ShowToast(
                         UiText.DynamicString(
                             "${userResponse.message}"
                         )
                     )
                 )*/
            }
            is ResultWrapper.NetworkError -> {
                _state.value = _state.value.copy(isLoading = false)//,myLeaguesList = arrayListOf())
                /*_channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            userResponse.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                _state.value = _state.value.copy(isLoading = false)
                userResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _state.value = _state.value.copy(myLeaguesList = response.data)
                    } else {
                        _state.value = _state.value.copy(myLeaguesList = arrayListOf())
                        /* _channel.send(
                             EventChannel.ShowToast(
                                 UiText.DynamicString(
                                     response.statusMessage
                                 )
                             )
                         )*/
                    }
                }
            }
        }
    }

    private suspend fun getDivisions() {
        _state.value = _state.value.copy(isLoading = true)
        val userResponse =
            teamRepo.getDivisions(
                gender = _state.value.gender,
                eventId = _state.value.eventId
            )
        _state.value = _state.value.copy(isLoading = false)

        when (userResponse) {
            is ResultWrapper.GenericError -> {
                _state.value = _state.value.copy(isLoading = false)
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${userResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _state.value = _state.value.copy(isLoading = false)
                /* _channel.send(
                     EventChannel.ShowToast(
                         UiText.DynamicString(
                             userResponse.message
                         )
                     )
                 )*/
            }
            is ResultWrapper.Success -> {
                _state.value = _state.value.copy(isLoading = false)
                userResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _state.value = _state.value.copy(divisions = response.data)
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

    private suspend fun getVenues() {
        _state.value = _state.value.copy(isLoading = true)
        val userResponse =
            teamRepo.getVenues(
                eventId = _state.value.eventId
            )
        /* _state.value = _state.value.copy(isLoading = false)*/

        when (userResponse) {
            is ResultWrapper.GenericError -> {
                _state.value = _state.value.copy(isLoading = false)
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${userResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _state.value = _state.value.copy(isLoading = false)
                /* _channel.send(
                     EventChannel.ShowToast(
                         UiText.DynamicString(
                             userResponse.message
                         )
                     )
                 )*/
            }
            is ResultWrapper.Success -> {
                _state.value = _state.value.copy(isLoading = false)
                userResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _state.value = _state.value.copy(venuesList = response.data.event.venuesId)
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

    private suspend fun getTeamsByLeagueAndDivision(divisionId: String) {
        _state.value = _state.value.copy(isLoading = true)
        val teamResponse =
            eventsRepo.getTeamsByLeagueAndDivision(
                eventId = _state.value.eventId,
                divisionId = divisionId
            )
        _state.value = _state.value.copy(isLoading = false)

        when (teamResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    EventChannel.ShowDivisionTeamToast(
                        UiText.DynamicString(
                            "${teamResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                /* _channel.send(
                     EventChannel.ShowDivisionTeamToast(
                         UiText.DynamicString(
                             teamResponse.message
                         )
                     )
                 )*/
            }
            is ResultWrapper.Success -> {
                teamResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _state.value = _state.value.copy(teamsByLeagueDivision = response.data)
                    } else {
                        _channel.send(
                            EventChannel.ShowDivisionTeamToast(
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

    private suspend fun getTeamsByLeagueIdAllDivision(eventId: String) {
        _state.value = _state.value.copy(isLoading = true)
        val teamResponse =
            eventsRepo.getTeamsByLeagueIdAllDivision(
                eventId = eventId
            )
        _state.value = _state.value.copy(isLoading = false)

        when (teamResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${teamResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                /* _channel.send(
                     EventChannel.ShowToast(
                         UiText.DynamicString(
                             teamResponse.message
                         )
                     )
                 )*/
            }
            is ResultWrapper.Success -> {
                teamResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _state.value = _state.value.copy(divisionWiseTeamResponse = response.data)
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

    private suspend fun getAllTeamsStandingByLeaguedAndDivision(
        eventId: String,
        divisionId: String
    ) {
        _state.value = _state.value.copy(isLoading = true)
        val standingResponse =
            eventsRepo.getAllTeamsStandingByLeaguedAndDivision(
                eventId = eventId,
                divisionId = divisionId
            )
        _state.value = _state.value.copy(isLoading = false)

        when (standingResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${standingResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                /*_channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            standingResponse.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                standingResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _state.value = _state.value.copy(
                            standingUIState = _state.value.standingUIState.copy(
                                allTeam = response.data.allTeams,
                                categories = response.data.categories
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

    private suspend fun getVenueDetailsById(venueId: String) {
        _state.value = _state.value.copy(isLoading = true)
        val venueResponse =
            eventsRepo.getVenueDetailsById(
                venueId = venueId,
                eventId = _state.value.eventId
            )
        _state.value = _state.value.copy(isLoading = false)

        when (venueResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${venueResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                /* _channel.send(
                     EventChannel.ShowToast(
                         UiText.DynamicString(
                             venueResponse.message
                         )
                     )
                 )*/
            }
            is ResultWrapper.Success -> {
                venueResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _state.value = _state.value.copy(venueDetails = response.data)
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
                /* _channel.send(
                     EventChannel.ShowEventDetailsToast(
                         UiText.DynamicString(
                             eventResponse.message
                         )
                     )
                 )*/
            }
            is ResultWrapper.Success -> {
                eventResponse.value.let { response ->
                    if (response.data != null) {
                        _state.value =
                            _state.value.copy(scheduleResponse = response.data.sortedByDescending {
                                convertStringDateToLong(it._id)
                            })
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

    private suspend fun getUserRoles() {
        _state.value = _state.value.copy(showLoading = true)

        when (val userRoles = UserStorage.role?.let { teamRepo.getUserRoles(it) }) {
            is ResultWrapper.GenericError -> {
                _state.value = _state.value.copy(showLoading = false)

                _channel.send(
                    EventChannel.ShowToast(
                        UiText.DynamicString(
                            "${userRoles.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _state.value = _state.value.copy(showLoading = false)

                /*  _channel.send(
                      EventChannel.ShowToast(
                          UiText.DynamicString(
                              userRoles.message
                          )
                      )
                  )*/
            }
            is ResultWrapper.Success -> {
                userRoles.value.let { response ->
                    if (response.status && response.data != null) {
                        _state.value =
                            _state.value.copy(
                                showLoading = false,
                                roles = response.data
                            )
                    } else {

                        _state.value =
                            _state.value.copy(
                                showLoading = false,
                            )

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
            else -> {}
        }
    }

    private fun getRefereeFilters() {

        val gender = arrayListOf(GenderList("Male"), GenderList("Female"))
        val eventType = arrayListOf(
            com.allballapp.android.data.response.EventType("League"),
            com.allballapp.android.data.response.EventType("Clinic"),
            com.allballapp.android.data.response.EventType("Camp")
        )
        val format = arrayListOf(Format("Indivisual"), Format("Team"))

        _state.value =
            _state.value.copy(genderList = gender, eventType = eventType, formatList = format)
    }

}


sealed class EventChannel {
    data class ShowToast(val message: UiText) : EventChannel()
    data class ShowDivisionTeamToast(val message: UiText) : EventChannel()
    data class ShowEventDetailsToast(val message: UiText) : EventChannel()
    data class OnUpdateNoteSuccess(val message: String) : EventChannel()
    data class OnSuccess(val message: UiText) : EventChannel()
}
