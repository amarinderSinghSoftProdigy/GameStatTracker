package com.softprodigy.ballerapp.ui.features.user_type.team_setup

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.baller_app.core.util.UiText
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.getFileFromUri
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.request.CreateTeamRequest
import com.softprodigy.ballerapp.data.response.Player
import com.softprodigy.ballerapp.domain.repository.IImageUploadRepo
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupTeamViewModel @Inject constructor(
    private val teamRepo: ITeamRepository,
    private val imageUploadRepo: IImageUploadRepo,
    private val dataStoreManager: DataStoreManager,
    application: Application
) : AndroidViewModel(application) {

    private val _teamSetupChannel = Channel<TeamSetupChannel>()
    val teamSetupChannel = _teamSetupChannel.receiveAsFlow()

    private val _teamSetupUiState = mutableStateOf(TeamSetupUIState())
    val teamSetupUiState: State<TeamSetupUIState> = _teamSetupUiState

    fun onEvent(event: TeamSetupUIEvent) {
        when (event) {
            is TeamSetupUIEvent.OnColorSelected -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(teamColor = event.selectedColor)
                viewModelScope.launch {
                    dataStoreManager.setColor(event.selectedColor)
                }
            }
            is TeamSetupUIEvent.OnImageSelected -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(teamImageUri = event.teamImageUri)

            }
            is TeamSetupUIEvent.OnTeamNameChange -> {
                _teamSetupUiState.value = _teamSetupUiState.value.copy(teamName = event.teamName)

            }
            TeamSetupUIEvent.OnTeamSetupNextClick -> {
                viewModelScope.launch {
                    _teamSetupChannel.send(TeamSetupChannel.OnTeamSetupNextClick)
                }
            }
            is TeamSetupUIEvent.OnSearchPlayer -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(search = event.searchPlayerQuery)
                if (event.searchPlayerQuery.length > 2) {
                    viewModelScope.launch {
                        getPlayers(event.searchPlayerQuery)
                    }
                }
            }
            is TeamSetupUIEvent.OnAddPlayerClick -> {
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
            is TeamSetupUIEvent.OnDismissDialogCLick -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(showDialog = event.showDialog)

            }
            is TeamSetupUIEvent.OnRemovePlayerClick -> {
                _teamSetupUiState.value = _teamSetupUiState.value.copy(
                    showDialog = true,
                    removePlayer = event.player,
                )

            }
            is TeamSetupUIEvent.OnRemovePlayerConfirmClick -> {
                _teamSetupUiState.value = _teamSetupUiState.value.copy(
                    showDialog = false,
                    selectedPlayers = (((_teamSetupUiState.value.selectedPlayers) - event.player) as ArrayList<Player>)
                )

            }
            TeamSetupUIEvent.OnAddPlayerScreenNext -> {
                viewModelScope.launch {
                    uploadTeamLogo()
                }
            }
            TeamSetupUIEvent.OnLogoUploadSuccess -> {
                viewModelScope.launch { createTeam() }
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
                    if (response.status) {
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

        when (val uploadLogoResponse = imageUploadRepo.uploadSingleImage(
            type = AppConstants.TEAM_LOGO,
            file
        )) {
            is ResultWrapper.GenericError -> {
                _teamSetupChannel.send(
                    TeamSetupChannel.ShowToast(
                        UiText.DynamicString(
                            "${uploadLogoResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
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
                    if (response.status) {
                        _teamSetupUiState.value =
                            _teamSetupUiState.value.copy(teamImageServerUrl = "${BuildConfig.IMAGE_SERVER}${uploadLogoResponse.value.data.data}")
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

        val playersId = _teamSetupUiState.value.selectedPlayers.map {
            it.Id
        }
        val request = CreateTeamRequest(
            name = _teamSetupUiState.value.teamName,
            colorCode = _teamSetupUiState.value.teamColor,
            players = playersId as ArrayList<String>,
            coaches = arrayListOf("6304540bb9165453b9859fa1"),
            logo = _teamSetupUiState.value.teamImageServerUrl
        )

        when (val createTeamResponse = teamRepo.createTeamAPI(
            request
        )) {
            is ResultWrapper.GenericError -> {
                _teamSetupChannel.send(
                    TeamSetupChannel.ShowToast(
                        UiText.DynamicString(
                            "${createTeamResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
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
                    if (response.status) {
                        /*   _teamSetupUiState.value =
                               _teamSetupUiState.value.copy(teamImageUri = createTeamResponse.value.data)*/
                        _teamSetupChannel.send(
                            TeamSetupChannel.OnTeamCreate(response.data.Id)
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

}

sealed class TeamSetupChannel {
    data class ShowToast(val message: UiText) : TeamSetupChannel()
    object OnTeamSetupNextClick : TeamSetupChannel()
    object OnLogoUpload : TeamSetupChannel()
    data class OnTeamCreate(val teamId: String) : TeamSetupChannel()

}