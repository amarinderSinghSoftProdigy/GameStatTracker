package com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.getFileFromUri
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.request.CreateTeamRequest
import com.softprodigy.ballerapp.data.request.Location
import com.softprodigy.ballerapp.data.request.Members
import com.softprodigy.ballerapp.data.request.UpdateTeamRequest
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.domain.repository.IImageUploadRepo
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SetupTeamViewModelUpdated @Inject constructor(
    private val teamRepo: ITeamRepository,
    private val imageUploadRepo: IImageUploadRepo,
    private val dataStoreManager: DataStoreManager,
    application: Application
) : AndroidViewModel(application) {

    private val _teamSetupChannel = Channel<TeamSetupChannel>()
    val teamSetupChannel = _teamSetupChannel.receiveAsFlow()

    private val _teamSetupUiState = mutableStateOf(TeamSetupUIStateUpdated())
    val teamSetupUiState: State<TeamSetupUIStateUpdated> = _teamSetupUiState
    lateinit var context: Context

    init {
        context = application.applicationContext
        initialInviteCount()
    }

    fun initialInviteCount(size: Int = 2) {
        _teamSetupUiState.value =
            _teamSetupUiState.value.copy(inviteList = _teamSetupUiState.value.inviteList.apply {
                this.clear()
            })

        _teamSetupUiState.value =
            _teamSetupUiState.value.copy(inviteList = _teamSetupUiState.value.inviteList
                .apply {
                    for (i in 0 until size) {
                        add(InviteObject())
                    }
                })
    }

    fun onEvent(event: TeamSetupUIEventUpdated) {
        when (event) {
            is TeamSetupUIEventUpdated.GetRoles -> {
                viewModelScope.launch {
                    getUserRoles()
                }
            }
            is TeamSetupUIEventUpdated.GetInvitedTeamPlayers -> {
                viewModelScope.launch {
                    getInvitePlayers(event.teamId)
                }
            }
            is TeamSetupUIEventUpdated.OnColorSelected -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(teamColorPrimary = event.primaryColor)
                viewModelScope.launch {
                    dataStoreManager.setColor(event.primaryColor)
                }
            }
            is TeamSetupUIEventUpdated.OnSecColorSelected -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(teamColorSec = event.secondaryColor)
            }
            is TeamSetupUIEventUpdated.OnTerColorSelected -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(teamColorThird = event.ternaryColor)
            }

            is TeamSetupUIEventUpdated.OnImageSelected -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(teamImageUri = event.teamImageUri)

            }
            is TeamSetupUIEventUpdated.OnTeamNameChange -> {
                _teamSetupUiState.value = _teamSetupUiState.value.copy(teamName = event.teamName)

            }
            TeamSetupUIEventUpdated.OnTeamSetupNextClick -> {
                viewModelScope.launch {
                    _teamSetupChannel.send(TeamSetupChannel.OnTeamSetupNextClick)
                }
            }
            is TeamSetupUIEventUpdated.OnSearchPlayer -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(search = event.searchPlayerQuery)
                viewModelScope.launch {
                    getPlayers(event.searchPlayerQuery)
                }
            }
            is TeamSetupUIEventUpdated.OnAddPlayerClick -> {
                if (!_teamSetupUiState.value.selectedPlayers.contains(event.player)) {
                    _teamSetupUiState.value =
                        _teamSetupUiState.value.copy(selectedPlayers = (((_teamSetupUiState.value.selectedPlayers) + event.player) as ArrayList<Player>))
                } else {
                    viewModelScope.launch {
                        _teamSetupChannel.send(
                            TeamSetupChannel.ShowToast(
                                UiText.StringResource(
                                    R.string.player_already_added
                                )
                            )
                        )
                    }
                }
            }
            is TeamSetupUIEventUpdated.OnDismissDialogCLick -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(showDialog = event.showDialog)

            }
            is TeamSetupUIEventUpdated.OnRemovePlayerClick -> {
                _teamSetupUiState.value = _teamSetupUiState.value.copy(
                    showDialog = true,
                    removePlayer = event.player,
                )

            }
            is TeamSetupUIEventUpdated.OnRemovePlayerConfirmClick -> {
                _teamSetupUiState.value = _teamSetupUiState.value.copy(
                    showDialog = false,
                    selectedPlayers = (((_teamSetupUiState.value.selectedPlayers) - event.player) as ArrayList<Player>)
                )

            }
            TeamSetupUIEventUpdated.OnAddPlayerScreenNext -> {
                viewModelScope.launch {
                    uploadTeamLogo()
                }
            }
            TeamSetupUIEventUpdated.OnLogoUploadSuccess -> {
                viewModelScope.launch { createTeam() }
            }
            is TeamSetupUIEventUpdated.OnContactAdded -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(
                        inviteList = _teamSetupUiState.value.inviteList.apply {
                            this[_teamSetupUiState.value.index].name = event.data.name
                            this[_teamSetupUiState.value.index].contact =
                                event.data.contact.takeLast(10)
                        }
                    )
                /* To achieve recomposition only*/

                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(inviteList = _teamSetupUiState.value.inviteList
                        .apply {
                            add(InviteObject())
                        })

                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(inviteList = _teamSetupUiState.value.inviteList
                        .apply {
                            removeLast()
                        })
            }

            is TeamSetupUIEventUpdated.OnIndexChange -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(
                        index = event.index
                    )
            }
            is TeamSetupUIEventUpdated.OnNameValueChange -> {

                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(inviteList = _teamSetupUiState.value.inviteList
                        .apply {
                            this[event.index].name = event.name
                        })

                /* To achieve recomposition only*/

                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(inviteList = _teamSetupUiState.value.inviteList
                        .apply {
                            add(InviteObject())
                        })

                event.index.let {
                    _teamSetupUiState.value =
                        _teamSetupUiState.value.copy(inviteList = _teamSetupUiState.value.inviteList
                            .apply {
                                removeLast()
                            })
                }


            }
            is TeamSetupUIEventUpdated.OnEmailValueChange -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(inviteList = _teamSetupUiState.value.inviteList
                        .apply {
                            this[event.index].contact = event.email
                        })

                /* To achieve recomposition only*/

                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(inviteList = _teamSetupUiState.value.inviteList
                        .apply {
                            add(InviteObject())
                        })

                event.index.let {
                    _teamSetupUiState.value =
                        _teamSetupUiState.value.copy(inviteList = _teamSetupUiState.value.inviteList
                            .apply {
                                removeLast()
                            })
                }

            }

            is TeamSetupUIEventUpdated.OnCountryValueChange -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(inviteList = _teamSetupUiState.value.inviteList
                        .apply {
                            this[event.index].countryCode = event.code
                        })
            }
            is TeamSetupUIEventUpdated.OnRoleValueChange -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(inviteList = _teamSetupUiState.value.inviteList
                        .apply {
                            this[event.index].role = event.role
                        })

            }

            is TeamSetupUIEventUpdated.OnInviteCountValueChange -> {
                if (event.addIntent) {
                    _teamSetupUiState.value =
                        _teamSetupUiState.value.copy(inviteList = _teamSetupUiState.value.inviteList
                            .apply {
                                add(InviteObject())
                            })
                    /*_teamSetupUiState.value.inviteMemberName.add("")
                    _teamSetupUiState.value.inviteMemberEmail.add("")*/
                } else {
                    event.index?.let {
                        _teamSetupUiState.value =
                            _teamSetupUiState.value.copy(inviteList = _teamSetupUiState.value.inviteList
                                .apply {
                                    removeAt(it)
                                })
                        /*_teamSetupUiState.value =
                        _teamSetupUiState.value.copy(inviteMemberCount = _teamSetupUiState.value.inviteMemberCount - 1)*/
                        /*_teamSetupUiState.value.inviteMemberName.removeAt(event.index)
                        _teamSetupUiState.value.inviteMemberEmail.removeAt(event.index)*/
                    }
                }


            }
            is TeamSetupUIEventUpdated.OnInviteTeamMembers -> {
                viewModelScope.launch {
                    invitePlayers(
                        event.teamId,
                        userType = event.userType,
                        type = event.type,
                        profileSelected = event.profilesSelected,
                        member = event.member,
                    )
                }
            }

            TeamSetupUIEventUpdated.OnBackButtonClickFromPlayerScreen -> {
                resetMemberValues()
            }

            is TeamSetupUIEventUpdated.OnTeamNameJerseyChange -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(teamNameOnJerseys = event.teamNameOnJersey)

            }
            is TeamSetupUIEventUpdated.OnTeamNameTournamentsChange -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(teamNameOnTournaments = event.teamNameOnTournaments)

            }
            is TeamSetupUIEventUpdated.OnVenueChange -> {
                _teamSetupUiState.value = _teamSetupUiState.value.copy(venueName = event.venueName)

            }

            is TeamSetupUIEventUpdated.OnCoachNameChange -> {
                _teamSetupUiState.value = _teamSetupUiState.value.copy(coachName = event.coachName)
            }
            is TeamSetupUIEventUpdated.OnCoachEmailChange -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(coachEmail = event.coachEmail)
            }
            is TeamSetupUIEventUpdated.OnCoachRoleChange -> {
                _teamSetupUiState.value = _teamSetupUiState.value.copy(coachRole = event.coachRole)
            }

            is TeamSetupUIEventUpdated.OnAddressChanged -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(selectedAddress = event.addressReq)
            }
        }


    }


    private suspend fun getUserRoles() {
        _teamSetupUiState.value = _teamSetupUiState.value.copy(isLoading = true)
        val userRoles = teamRepo.getUserRoles("")
        _teamSetupUiState.value = _teamSetupUiState.value.copy(isLoading = false)
        when (userRoles) {
            is ResultWrapper.GenericError -> {
                _teamSetupChannel.send(
                    TeamSetupChannel.ShowToast(
                        UiText.DynamicString(
                            "${userRoles.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _teamSetupChannel.send(
                    TeamSetupChannel.ShowToast(
                        UiText.DynamicString(
                            userRoles.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                userRoles.value.let { response ->
                    if (response.status && response.data != null) {
                        _teamSetupUiState.value =
                            _teamSetupUiState.value.copy(
                                roles = response.data
                            )
                    } else {
                        _teamSetupChannel.send(
                            TeamSetupChannel.ShowToast(
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


    private fun resetMemberValues() {
        _teamSetupUiState.value =
            _teamSetupUiState.value.copy(
                inviteList = mutableStateListOf()
                /*inviteMemberCount = 3,
                inviteMemberName = arrayListOf("", "", ""),
                inviteMemberEmail = arrayListOf("", "", "")*/
            )
    }

    private suspend fun getInvitePlayers(teamId: String) {
        _teamSetupUiState.value =
            _teamSetupUiState.value.copy(isLoading = true)

        val inviteMemberResponse = teamRepo.getInviteMembersByTeamId(teamId)

        _teamSetupUiState.value =
            _teamSetupUiState.value.copy(isLoading = false)
        when (inviteMemberResponse) {
            is ResultWrapper.GenericError -> {
                _teamSetupChannel.send(
                    TeamSetupChannel.ShowToast(
                        UiText.DynamicString(
                            "${inviteMemberResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _teamSetupChannel.send(
                    TeamSetupChannel.ShowToast(
                        UiText.DynamicString(
                            inviteMemberResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                inviteMemberResponse.value.let { response ->
                    if (response.data != null) {
                        _teamSetupUiState.value =
                            _teamSetupUiState.value.copy(memberList = response.data)
                    } else {
                        _teamSetupUiState.value =
                            _teamSetupUiState.value.copy(isLoading = false)
                        _teamSetupChannel.send(
                            TeamSetupChannel.ShowToast(
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

    private suspend fun invitePlayers(
        teamId: String,
        type: String,
        userType: String,
        profileSelected: Boolean,
        member: Members?
    ) {


        val members = if (member != null)
            listOf(member)
        else {
            val list = _teamSetupUiState.value.inviteList
            for (item in list) {
                if (item.contact.isEmpty()) {
                    list.remove(item)
                }
            }
            list.mapIndexed { index, item ->
                Members(
                    name = item.name,
                    mobileNumber = "${item.countryCode}${item.contact}",
                    role = item.role.key
                )
            }
        }


        val request = UpdateTeamRequest(
            teamID = teamId,
            members = members,
            type = type,
            userType = userType,
            profilesSelected = profileSelected
        )

        _teamSetupUiState.value =
            _teamSetupUiState.value.copy(isLoading = true)

        val inviteMemberResponse = teamRepo.inviteMembersByTeamId(request)

        _teamSetupUiState.value =
            _teamSetupUiState.value.copy(isLoading = false)
        when (inviteMemberResponse) {
            is ResultWrapper.GenericError -> {
                _teamSetupChannel.send(
                    TeamSetupChannel.ShowToast(
                        UiText.DynamicString(
                            "${inviteMemberResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _teamSetupChannel.send(
                    TeamSetupChannel.ShowToast(
                        UiText.DynamicString(
                            inviteMemberResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                inviteMemberResponse.value.let { response ->
                    if (response.status && response.data.failedMobileNumber.isEmpty() && response.data.failedProfiles.isEmpty() ) {
                        _teamSetupChannel.send(
                            TeamSetupChannel.OnInvitationSuccess(
                                UiText.DynamicString(
                                    response.statusMessage
                                )
                            )
                        )
                        resetMemberValues()
                        inItToDefaultData()
                        _teamSetupChannel.send(
                            TeamSetupChannel.ShowToast(
                                UiText.DynamicString(
                                    response.statusMessage
                                )
                            )
                        )
                    } else if (response.data.failedMobileNumber.isNotEmpty() || response.data.failedProfiles.isNotEmpty()) {
                        _teamSetupChannel.send(
                            TeamSetupChannel.ShowToast(
                                UiText.DynamicString(
                                    context.getString(R.string.number_already_exist) + " " + response.data.failedMobileNumber.toString() + response.data.failedProfiles.toString()
                                        .replace("[", "").replace("]", "")
                                )
                            )
                        )
                    } else {
                        _teamSetupUiState.value =
                            _teamSetupUiState.value.copy(isLoading = false)
                        _teamSetupChannel.send(
                            TeamSetupChannel.ShowToast(
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

    private suspend fun getPlayers(searchQuery: String) {

        when (val playersResponse = teamRepo.getAllPlayers(searchPlayer = searchQuery)) {
            is ResultWrapper.GenericError -> {
                _teamSetupChannel.send(
                    TeamSetupChannel.ShowToast(
                        UiText.DynamicString(
                            "${playersResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _teamSetupChannel.send(
                    TeamSetupChannel.ShowToast(
                        UiText.DynamicString(
                            playersResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                playersResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _teamSetupUiState.value =
                            _teamSetupUiState.value.copy(
                                players = response.data,
                                isLoading = false
                            )
                    } else {
                        _teamSetupUiState.value =
                            _teamSetupUiState.value.copy(isLoading = false)
                        _teamSetupChannel.send(
                            TeamSetupChannel.ShowToast(
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

    private suspend fun uploadTeamLogo() {
        val uri = Uri.parse(teamSetupUiState.value.teamImageUri)

        val file = getFileFromUri(getApplication<Application>().applicationContext, uri)

        if (file != null) {
            val size = Integer.parseInt((file.length() / 1024).toString())
            Timber.i("Filesize compressed --> $size")
        }

        val uploadLogoResponse = imageUploadRepo.uploadSingleImage(
            type = AppConstants.TEAM_LOGO,
            file
        )


        when (uploadLogoResponse) {
            is ResultWrapper.GenericError -> {
                _teamSetupUiState.value = _teamSetupUiState.value.copy(isLoading = false)
                _teamSetupChannel.send(
                    TeamSetupChannel.ShowToast(
                        UiText.DynamicString(
                            "${uploadLogoResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _teamSetupUiState.value = _teamSetupUiState.value.copy(isLoading = false)
                _teamSetupChannel.send(
                    TeamSetupChannel.ShowToast(
                        UiText.DynamicString(
                            uploadLogoResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                uploadLogoResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _teamSetupUiState.value =
                            _teamSetupUiState.value.copy(
                                isLoading = false,
                                teamImageServerUrl = uploadLogoResponse.value.data.data
                            )
                        _teamSetupChannel.send(
                            TeamSetupChannel.OnLogoUpload
                        )
                    } else {
                        _teamSetupUiState.value =
                            _teamSetupUiState.value.copy(isLoading = false)
                        _teamSetupChannel.send(
                            TeamSetupChannel.ShowToast(
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

    private suspend fun createTeam() {
        val members = _teamSetupUiState.value.inviteList.mapIndexed { index, item ->
            Members(
                name = item.name,
                mobileNumber = "${item.countryCode}${item.contact}",
                role = item.role.key
            )
        }
        val location = Location(
            type = "Point",
            coordinates = arrayListOf(
                _teamSetupUiState.value.selectedAddress.long,
                _teamSetupUiState.value.selectedAddress.lat,
            )
        )
        val request = CreateTeamRequest(
            name = _teamSetupUiState.value.teamName,
            teamNameOnJersey = _teamSetupUiState.value.teamNameOnJerseys,
            teamNameOnTournaments = _teamSetupUiState.value.teamNameOnTournaments,
            nameOfVenue = _teamSetupUiState.value.venueName,
            address = _teamSetupUiState.value.selectedAddress,
            location = location,
            colorCode = "#" + _teamSetupUiState.value.teamColorPrimary,
            primaryTeamColor = "#" + _teamSetupUiState.value.teamColorPrimary,
            secondaryTeamColor = "#" + _teamSetupUiState.value.teamColorSec,
            tertiaryTeamColor = "#" + _teamSetupUiState.value.teamColorThird,
            logo = _teamSetupUiState.value.teamImageServerUrl,
            members = members,
        )

        _teamSetupUiState.value = _teamSetupUiState.value.copy(isLoading = true)

        val createTeamResponse = teamRepo.createTeamAPI(
            request
        )

        when (createTeamResponse) {
            is ResultWrapper.GenericError -> {
                _teamSetupUiState.value = _teamSetupUiState.value.copy(isLoading = false)
                _teamSetupChannel.send(
                    TeamSetupChannel.ShowToast(
                        UiText.DynamicString(
                            "${createTeamResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _teamSetupUiState.value = _teamSetupUiState.value.copy(isLoading = false)
                _teamSetupChannel.send(
                    TeamSetupChannel.ShowToast(
                        UiText.DynamicString(
                            createTeamResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                createTeamResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _teamSetupUiState.value = _teamSetupUiState.value.copy(isLoading = false)
                        _teamSetupChannel.send(
                            TeamSetupChannel.OnTeamCreate(response.statusMessage)
                        )
                        inItToDefaultData()
                    } else {
                        _teamSetupUiState.value =
                            _teamSetupUiState.value.copy(isLoading = false)
                        _teamSetupChannel.send(
                            TeamSetupChannel.ShowToast(
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

    private fun clearToken() {
        viewModelScope.launch {
            dataStoreManager.saveToken("")
            dataStoreManager.setRole("")
            dataStoreManager.setEmail("")
        }
    }

    private fun inItToDefaultData() {
        Log.i("inItToDefaultData", "inItToDefaultData: ")
        _teamSetupUiState.value =
            TeamSetupUIStateUpdated(teamColorPrimary = _teamSetupUiState.value.teamColorPrimary)
    }
}

sealed class TeamSetupChannel {
    data class ShowToast(val message: UiText) : TeamSetupChannel()
    data class OnInvitationSuccess(val message: UiText) : TeamSetupChannel()
    object OnTeamSetupNextClick : TeamSetupChannel()
    object OnLogoUpload : TeamSetupChannel()
    data class OnTeamCreate(val message: String) : TeamSetupChannel()

}

fun <T> List<T>.updateElement(predicate: (T) -> Boolean, transform: (T) -> T): List<T> {
    return map { if (predicate(it)) transform(it) else it }
}