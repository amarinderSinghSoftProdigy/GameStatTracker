package com.allballapp.android.ui.features.home.teams

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.allballapp.android.common.AppConstants
import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.common.getFileFromUri
import com.allballapp.android.core.util.UiText
import com.allballapp.android.data.UserStorage
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.data.request.Location
import com.allballapp.android.data.request.UpdateTeamDetailRequest
import com.allballapp.android.data.response.SwapUser
import com.allballapp.android.data.response.team.Player
import com.allballapp.android.data.response.team.Team
import com.allballapp.android.data.response.team.TeamRoaster
import com.allballapp.android.domain.repository.IImageUploadRepo
import com.allballapp.android.domain.repository.ITeamRepository
import com.allballapp.android.ui.features.components.fromHex
import com.allballapp.android.ui.utils.CommonUtils
import com.allballapp.android.ui.utils.dragDrop.ItemPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val teamRepo: ITeamRepository,
    private val imageUploadRepo: IImageUploadRepo,
    private val dataStoreManager: DataStoreManager,
    application: Application
) : AndroidViewModel(application) {
    var searchJob: Job? = null
    private val _teamChannel = Channel<TeamChannel>()
    val teamChannel = _teamChannel.receiveAsFlow()

    private val _teamUiState = mutableStateOf(TeamUIState())
    val teamUiState: State<TeamUIState> = _teamUiState

    fun isDragEnabled(pos: ItemPosition) = true

    fun isRoasterDragEnabled(pos: ItemPosition) =
        _teamUiState.value.players.toMutableList().getOrNull(pos.index)?.locked != true

    fun isUserDragEnabled(pos: ItemPosition) = true

    fun moveItem(from: ItemPosition, to: ItemPosition) {
        _teamUiState.value =
            _teamUiState.value.copy(
                leaderBoard = _teamUiState.value.leaderBoard.toMutableList()
                    .apply {
                        add(to.index, removeAt(from.index))
                    }
            )
    }

    fun moveItemRoaster(from: ItemPosition, to: ItemPosition) {
        _teamUiState.value =
            _teamUiState.value.copy(
                players = _teamUiState.value.players.toMutableList()
                    .apply {
                        add(to.index, removeAt(from.index))
                    } as ArrayList<Player>
            )
    }

    fun moveItemUser(from: ItemPosition, to: ItemPosition) {
        _teamUiState.value =
            _teamUiState.value.copy(
                acceptPending = _teamUiState.value.acceptPending.toMutableList()
                    .apply {
                        add(to.index, removeAt(from.index))
                    } as ArrayList<SwapUser>
            )
    }

    private suspend fun updateSelection(name: String): Boolean {
        if (name == "All") {
            _teamUiState.value.leaderBoard.forEach {
                it.status = true
            }
        } else if (name.isEmpty()) {
            _teamUiState.value.leaderBoard.forEach {
                it.status = false
            }
        } else {
            _teamUiState.value.leaderBoard.forEach {
                if (it.name == name) {
                    it.status = !it.status
                }
            }
        }
        val count =
            CommonUtils.getSelectedList(_teamUiState.value.leaderBoard)

        _teamUiState.value =
            _teamUiState.value.copy(
                all = count,
                leaderBoard = _teamUiState.value.leaderBoard
            )
        delay(200)
        return true
    }

    fun onEvent(event: TeamUIEvent) {
        when (event) {
            is TeamUIEvent.OnTeamIdSelected -> {
                viewModelScope.launch {
                    getPlayerById(event.teamId)
                }
            }
            is TeamUIEvent.OnConfirmTeamClick -> {
                viewModelScope.launch {
                    dataStoreManager.setId(event.teamId)
                    UserStorage.teamId = event.teamId

                    dataStoreManager.setTeamName(event.teamName)
                    UserStorage.teamName = event.teamName

                    getTeamByTeamId(event.teamId)
                }
            }
            is TeamUIEvent.OnDismissClick -> {}

            is TeamUIEvent.OnTeamUpdate -> {
                viewModelScope.launch {
                    uploadTeamLogo()
                }
            }

            is TeamUIEvent.ShowToast -> {
                viewModelScope.launch {
                    showToast(event.message)
                }
            }
            is TeamUIEvent.OnTeamSelected -> {
                _teamUiState.value = _teamUiState.value.copy(selectedTeam = event.team)
            }
            is TeamUIEvent.OnColorSelected -> {
                _teamUiState.value =
                    _teamUiState.value.copy(teamColorPrimary = event.selectedColor)
            }
            is TeamUIEvent.OnSecColorSelected -> {
                _teamUiState.value =
                    _teamUiState.value.copy(teamColorSec = event.secondaryColor)
            }
            is TeamUIEvent.OnTerColorSelected -> {
                _teamUiState.value =
                    _teamUiState.value.copy(teamColorThird = event.ternaryColor)
            }

            is TeamUIEvent.OnImageSelected -> {
                _teamUiState.value =
                    _teamUiState.value.copy(localLogo = event.teamImageUri)
            }

            is TeamUIEvent.OnTeamNameChange -> {
                _teamUiState.value = _teamUiState.value.copy(teamName = event.teamName)
            }

            is TeamUIEvent.OnItemSelected -> {
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    updateSelection(event.name)
                }
            }
            is TeamUIEvent.OnTeamNameJerseyChange -> {
                _teamUiState.value =
                    _teamUiState.value.copy(teamNameOnJerseys = event.teamNameOnJersey)

            }
            is TeamUIEvent.OnTeamNameTournamentsChange -> {
                _teamUiState.value =
                    _teamUiState.value.copy(teamNameOnTournaments = event.teamNameOnTournaments)

            }
            is TeamUIEvent.OnVenueChange -> {
                _teamUiState.value = _teamUiState.value.copy(venueName = event.venueName)

            }
            is TeamUIEvent.OnAddressChanged -> {
                _teamUiState.value =
                    _teamUiState.value.copy(selectedAddress = event.addressReq)
            }
        }
    }

    private suspend fun showToast(message: String) {
        _teamChannel.send(
            TeamChannel.ShowToast(
                UiText.DynamicString(
                    message
                )
            )
        )
    }

    suspend fun getTeams() {
        getTeamsUserId()
        /* if (UserStorage.role.equals(UserType.COACH.key, ignoreCase = true)) {
             when (val teamResponse = teamRepo.getTeams(UserStorage.userId)) {
                 is ResultWrapper.GenericError -> {
                     _teamChannel.send(
                         TeamChannel.ShowToast(
                             UiText.DynamicString(
                                 "${teamResponse.message}"
                             )
                         )
                     )
                     _teamUiState.value =
                         _teamUiState.value.copy(
                             isLoading = false
                         )
                 }
                 is ResultWrapper.NetworkError -> {
                     _teamChannel.send(
                         TeamChannel.ShowToast(
                             UiText.DynamicString(
                                 teamResponse.message
                             )
                         )
                     )
                     _teamUiState.value =
                         _teamUiState.value.copy(
                             isLoading = false
                         )
                 }
                 is ResultWrapper.Success -> {
                     teamResponse.value.let { response ->
                         if (response.status) {
                             if (*//*_teamUiState.value.selectedTeam == null && *//*response.data.size > 0) {
                                var selectionTeam: Team? = null
                                response.data.toMutableList().forEach {
                                    if (UserStorage.teamId == it._id) {
                                        selectionTeam = it
                                    }
                                }
                                val idToSearch =
                                    if (selectionTeam == null) response.data[0]._id else selectionTeam?._id
                                _teamUiState.value =
                                    _teamUiState.value.copy(
                                        teams = response.data,
                                        selectedTeam = if (selectionTeam == null) response.data[0] else selectionTeam,
                                        isLoading = false,
                                        localLogo = null,
                                    )
                                viewModelScope.launch {
                                    if (!idToSearch.isNullOrEmpty()) {
                                        getTeamByTeamId(idToSearch)
                                        dataStoreManager.setId(idToSearch)
                                    }
                                }
                            }
                        } else {
                            _teamUiState.value =
                                _teamUiState.value.copy(isLoading = false)
                            _teamChannel.send(
                                TeamChannel.ShowToast(
                                    UiText.DynamicString(
                                        response.statusMessage
                                    )
                                )
                            )
                        }
                    }
                }
            }
        } else {
            getTeamsUserId()
        }*/
    }

    private suspend fun setDefaultTeam(team: Team) {
        _teamUiState.value = _teamUiState.value.copy(
            selectedTeam = team,
            teams = ArrayList<Team>().apply { add(team) },
            isLoading = false,
            teamName = team.name,
            teamColorPrimary = team.colorCode,
            logo = team.logo,
            teamColorSec = team.secondaryTeamColor,
            teamColorThird = team.tertiaryTeamColor,
            loadFirstUi = true,
        )
        _teamChannel.send(
            TeamChannel.OnTeamDetailsSuccess(team._id, team.name, false)
        )
        updateColorData(team.colorCode)
    }

    private suspend fun setDefaultData(team: Team) {
        _teamUiState.value = _teamUiState.value.copy(
            loadFirstUi = true,
            isLoading = false,
            teamName = team.name,
            teamColorPrimary = team.colorCode,
            logo = team.logo,
            teamColorSec = team.secondaryTeamColor,
            teamColorThird = team.tertiaryTeamColor,
        )
        _teamChannel.send(
            TeamChannel.OnTeamDetailsSuccess(team._id, team.name, false)
        )
        updateColorData(team.colorCode)
    }

    suspend fun getTeamsUserId() {
        when (val teamResponse = teamRepo.getTeamsUserId(UserStorage.userId)) {
            is ResultWrapper.GenericError -> {
                /* _teamChannel.send(
                     TeamChannel.ShowToast(
                         UiText.DynamicString(
                             "${teamResponse.message}"
                         )
                     )
                 )*/
                _teamUiState.value =
                    _teamUiState.value.copy(
                        isLoading = false
                    )
            }
            is ResultWrapper.NetworkError -> {
                _teamChannel.send(
                    TeamChannel.ShowToast(
                        UiText.DynamicString(
                            teamResponse.message
                        )
                    )
                )
                _teamUiState.value =
                    _teamUiState.value.copy(
                        isLoading = false
                    )
            }
            is ResultWrapper.Success -> {
                teamResponse.value.let { response ->
                    if (response.status) {
                        if (response.data.result.size > 0) {
                            if (response.data.result.size == 1) {
                                setRole(response.data.result[0].role)
                                setDefaultTeam(response.data.result[0].teamId)
                                return
                            }
                            var selectionTeam: Team? = null
                            response.data.result.toMutableList().forEach {
                                if (UserStorage.teamId.isEmpty()) {
                                    if (response.data.teamId == it.teamId._id) {
                                        selectionTeam = it.teamId
                                        setRole(it.role)
                                    }
                                } else {
                                    if (UserStorage.teamId == it.teamId._id) {
                                        selectionTeam = it.teamId
                                        setRole(it.role)
                                    }
                                }
                            }

                            val idToSearch =
                                if (selectionTeam == null) response.data.result[0].teamId._id else selectionTeam?._id
                            _teamUiState.value =
                                _teamUiState.value.copy(
                                    teams = CommonUtils.getTeams(response.data.result),
                                    selectedTeam = if (selectionTeam == null) response.data.result[0].teamId else selectionTeam,
                                    isLoading = false,
                                    localLogo = null,
                                    loadFirstUi = selectionTeam == null,
                                    teamId = response.data.teamId
                                )
                            if (selectionTeam == null) {
                                setRole(response.data.result[0].role)
                                setDefaultData(response.data.result[0].teamId)
                            } else {
                                viewModelScope.launch {
                                    if (!idToSearch.isNullOrEmpty()) {
                                        getTeamByTeamId(idToSearch)
                                        dataStoreManager.setId(idToSearch)
                                    }
                                }
                            }
                        }
                    } else {
                        _teamUiState.value =
                            _teamUiState.value.copy(isLoading = false)
                        _teamChannel.send(
                            TeamChannel.ShowToast(
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

    private suspend fun updateTeam() {
        _teamUiState.value =
            _teamUiState.value.copy(
                isLoading = true
            )

        val location = Location(
            type = "Point",
            coordinates = arrayListOf(
                _teamUiState.value.selectedAddress.lat,
                _teamUiState.value.selectedAddress.long
            )
        )

        val newRoaster = arrayListOf<TeamRoaster>()
        _teamUiState.value.players.forEach {
            //val position = it.position.ifEmpty { it._id }
            /* if (it.position.isNotEmpty())*/
            newRoaster.add(TeamRoaster(it._id, ""))
        }
        _teamUiState.value = _teamUiState.value.copy(newTeamId = UserStorage.teamId)
        //Need to update the request object.
        val request = UpdateTeamDetailRequest(
            id = UserStorage.teamId,
            leaderboardPoints = _teamUiState.value.leaderBoard,
            playerPositions = newRoaster,
            name = _teamUiState.value.teamName,
            logo = _teamUiState.value.logo ?: "",
            colorCode = _teamUiState.value.teamColorPrimary,
            primaryTeamColor = _teamUiState.value.teamColorPrimary,

            nameOfVenue = _teamUiState.value.venueName,
            secondaryTeamColor = _teamUiState.value.teamColorSec,
            tertiaryTeamColor = _teamUiState.value.teamColorThird,
            address = _teamUiState.value.selectedAddress,
            location = location,
            teamNameOnJersey = _teamUiState.value.teamNameOnJerseys,
            teamNameOnTournaments = _teamUiState.value.teamNameOnTournaments,


            )
        _teamUiState.value = _teamUiState.value.copy(updatedTeam = request)
        when (val teamResponse = teamRepo.updateTeamDetails(request)) {
            is ResultWrapper.GenericError -> {
                _teamChannel.send(
                    TeamChannel.ShowToast(
                        UiText.DynamicString(
                            "${teamResponse.message}"
                        )
                    )
                )
                _teamUiState.value =
                    _teamUiState.value.copy(
                        isLoading = false
                    )
            }
            is ResultWrapper.NetworkError -> {
                _teamChannel.send(
                    TeamChannel.ShowToast(
                        UiText.DynamicString(
                            teamResponse.message
                        )
                    )
                )
                _teamUiState.value =
                    _teamUiState.value.copy(
                        isLoading = false
                    )
            }
            is ResultWrapper.Success -> {
                teamResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _teamUiState.value =
                            _teamUiState.value.copy(
                                isLoading = false,
                                selectedTeam = teamResponse.value.data,
                                localLogo = null,
                            )
                        _teamChannel.send(
                            TeamChannel.OnTeamsUpdate(
                                UiText.DynamicString(
                                    response.statusMessage
                                ),
                                response.data._id,
                                response.data.name
                            )
                        )
                        viewModelScope.launch {
                            dataStoreManager.setColor(response.data.colorCode)
                        }
                    } else {
                        _teamUiState.value =
                            _teamUiState.value.copy(isLoading = false)
                        _teamChannel.send(
                            TeamChannel.ShowToast(
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
        _teamUiState.value = _teamUiState.value.copy(isLoading = true)
        val isLocalImageTaken = _teamUiState.value.localLogo != null

        if (isLocalImageTaken) {
            val uri = Uri.parse(teamUiState.value.localLogo)

            val file = getFileFromUri(getApplication<Application>().applicationContext, uri)

            if (file != null) {
                val size = Integer.parseInt((file.length() / 1024).toString())
                Timber.i("Filesize compressed --> $size")
            }
            val uploadLogoResponse = imageUploadRepo.uploadSingleImage(
                type = AppConstants.TEAM_LOGO,
                file
            )
            _teamUiState.value = _teamUiState.value.copy(isLoading = false)

            when (uploadLogoResponse) {
                is ResultWrapper.GenericError -> {
                    _teamChannel.send(
                        TeamChannel.ShowToast(
                            UiText.DynamicString(
                                "${uploadLogoResponse.message}"
                            )
                        )
                    )
                }
                is ResultWrapper.NetworkError -> {
                    _teamChannel.send(
                        TeamChannel.ShowToast(
                            UiText.DynamicString(
                                uploadLogoResponse.message
                            )
                        )
                    )
                }
                is ResultWrapper.Success -> {
                    uploadLogoResponse.value.let { response ->
                        if (response.status && response.data != null) {
                            _teamUiState.value =
                                _teamUiState.value.copy(
                                    logo = uploadLogoResponse.value.data.data,
                                    localLogo = null
                                )
                            updateTeam()
                        } else {
                            _teamUiState.value =
                                _teamUiState.value.copy(isLoading = false)
                            _teamChannel.send(
                                TeamChannel.ShowToast(
                                    UiText.DynamicString(
                                        response.statusMessage
                                    )
                                )
                            )
                        }
                    }
                }
            }
        } else {
            updateTeam()
        }
    }

    private suspend fun getTeamByTeamId(userId: String) {
        _teamUiState.value =
            _teamUiState.value.copy(isLoading = true)
        val teamResponse = teamRepo.getTeamsByTeamID(userId)
        Timber.i("getTeamByTeamId--teamViewModel")
        _teamUiState.value =
            _teamUiState.value.copy(isLoading = false)
        when (teamResponse) {
            is ResultWrapper.GenericError -> {
                if (_teamUiState.value.selectedTeam != null) {
                    setDefaultData(_teamUiState.value.selectedTeam!!)
                }
                _teamChannel.send(
                    TeamChannel.ShowToast(
                        UiText.DynamicString(
                            "${teamResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                if (_teamUiState.value.selectedTeam != null) {
                    setDefaultData(_teamUiState.value.selectedTeam!!)
                }
                _teamChannel.send(
                    TeamChannel.ShowToast(
                        UiText.DynamicString(
                            teamResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                teamResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _teamUiState.value = _teamUiState.value.copy(
                            isLoading = false,
                            players = CommonUtils.getPlayerTabs(response.data.players),
                            supportStaff = response.data.supportingCastDetails,
                            acceptPending = response.data.pendingAndDeclinedMembers,
                            coaches = response.data.coaches,
                            allUsers = response.data.allMembers,
                            teamName = response.data.name,
                            teamColorPrimary = response.data.colorCode,
                            logo = response.data.logo,
                            leaderBoard = response.data.teamLeaderBoard,
                            all = CommonUtils.getSelectedList(response.data.teamLeaderBoard),
                            teamColorSec = response.data.secondaryTeamColor,
                            teamColorThird = response.data.tertiaryTeamColor,
                            teamNameOnJerseys = response.data.teamNameOnJersey,
                            teamNameOnTournaments = response.data.teamNameOnTournaments,
                            venueName = response.data.nameOfVenue,
                            selectedAddress = response.data.address,
                        )
                        _teamChannel.send(
                            TeamChannel.OnTeamDetailsSuccess(
                                response.data._id,
                                response.data.name,
                                true
                            )
                        )
                        /*update Color code to db*/
                        updateColorData(response.data.colorCode)

                    } else {
                        if (_teamUiState.value.selectedTeam != null) {
                            setDefaultData(_teamUiState.value.selectedTeam!!)
                        }
                        _teamUiState.value =
                            _teamUiState.value.copy(isLoading = false)
                        _teamChannel.send(
                            TeamChannel.ShowToast(
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

    private suspend fun updateColorData(colorCode: String) {
        dataStoreManager.setColor(colorCode)
        AppConstants.SELECTED_COLOR =
            fromHex(colorCode.replace("#", "").ifEmpty { AppConstants.DEFAULT_COLOR })
    }

    private suspend fun getPlayerById(teamId: String) {
        _teamUiState.value = _teamUiState.value.copy(isLoading = true)

        when (val userRoles = teamRepo.getPlayerById(teamId, "true")) {
            is ResultWrapper.GenericError -> {
                _teamUiState.value = _teamUiState.value.copy(isLoading = false)

                _teamChannel.send(
                    TeamChannel.ShowToast(
                        UiText.DynamicString(
                            "${userRoles.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _teamUiState.value = _teamUiState.value.copy(isLoading = false)

                _teamChannel.send(
                    TeamChannel.ShowToast(
                        UiText.DynamicString(
                            userRoles.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                userRoles.value.let { response ->
                    if (response.status && response.data != null) {
                        _teamUiState.value =
                            _teamUiState.value.copy(
                                isLoading = false,
                                playersList = response.data
                            )
                    } else {

                        _teamUiState.value =
                            _teamUiState.value.copy(
                                isLoading = false,
                            )
                        _teamChannel.send(
                            TeamChannel.ShowToast(
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

    private fun setRole(role: String) {
        viewModelScope.launch {
            dataStoreManager.setRole(role)
        }
    }
}

sealed class TeamChannel {
    data class ShowToast(val message: UiText) : TeamChannel()
    data class OnTeamsUpdate(val message: UiText, val teamId: String, val teamName: String) :
        TeamChannel()

    data class OnTeamDetailsSuccess(
        val teamId: String,
        val teamName: String,
        val show: Boolean = true
    ) : TeamChannel()
}