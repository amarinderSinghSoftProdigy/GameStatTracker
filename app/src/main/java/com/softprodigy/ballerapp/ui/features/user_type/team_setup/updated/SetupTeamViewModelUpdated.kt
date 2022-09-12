package com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
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
import com.softprodigy.ballerapp.data.response.Player
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

    fun onEvent(event: TeamSetupUIEventUpdated) {
        when (event) {
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
            is TeamSetupUIEventUpdated.OnNameValueChange -> {
                /*_teamSetupUiState.value = _teamSetupUiState.value.copy(
                    inviteMemberName = (((_teamSetupUiState.value.inviteMemberName + _teamSetupUiState.value.inviteMemberName[event.index]) as MutableList<String>))
                )*/
                /*  _teamSetupUiState.value = _teamSetupUiState.value.copy(
                      inviteMemberName = (((_teamSetupUiState.value.inviteMemberName + _teamSetupUiState.value.inviteMemberName[event.index]) as MutableList<String>))
                  )*/
//                _teamSetupUiState.value.inviteMemberName.add(index = event.index,event.name)
//                _teamSetupUiState.value.inviteMemberName[event.index] = event.name
//                _teamSetupUiState.value = _teamSetupUiState.value.copy(
//                    inviteMemberName = _teamSetupUiState.value.inviteMemberName.add(index = event.index,event.name)
//                _teamSetupUiState.value = _teamSetupUiState.value.copy(
//                    inviteMemberName = (teamSetupUiState.value.inviteMemberName[event.index].apply {
//                        event.name
//                    }))
                _teamSetupUiState.value.inviteMemberName[event.index] = event.name

//                _teamSetupUiState.value=_teamSetupUiState.value.copy()

            }
            is TeamSetupUIEventUpdated.OnEmailValueChange -> {
                /* _teamSetupUiState.value = _teamSetupUiState.value.copy(
                     inviteMemberName = ((_teamSetupUiState.value.inviteMemberName + event.email) as MutableList<String>)
                 )*/
//                _teamSetupUiState.value.inviteMemberName.add(index = event.index,event.email)
                _teamSetupUiState.value.inviteMemberEmail[event.index] = event.email


            }
            is TeamSetupUIEventUpdated.OnInviteCountValueChange -> {

                _teamSetupUiState.value = _teamSetupUiState.value.copy(
                    inviteMemberName = (((_teamSetupUiState.value.inviteMemberName) + "") as ArrayList<String>),
                    inviteMemberEmail = (((_teamSetupUiState.value.inviteMemberEmail) + "") as ArrayList<String>)
                )

                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(inviteMemberCount = _teamSetupUiState.value.inviteMemberCount + 1)



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

        if (file != null) {
            val size = Integer.parseInt((file.length() / 1024).toString())
            Timber.i("Filesize compressed --> $size")
        }

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
                            _teamSetupUiState.value.copy(teamImageServerUrl = uploadLogoResponse.value.data.data)
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
            colorCode = _teamSetupUiState.value.teamColorPrimary,
            players = playersId as ArrayList<String>,
            coaches = arrayListOf("6315a53881aa3c6a26d51121"),
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

    private fun clearToken() {
        viewModelScope.launch {
            dataStoreManager.saveToken("")
            dataStoreManager.setRole("")
            dataStoreManager.setEmail("")
        }
    }
}

sealed class TeamSetupChannel {
    data class ShowToast(val message: UiText) : TeamSetupChannel()
    object OnTeamSetupNextClick : TeamSetupChannel()
    object OnLogoUpload : TeamSetupChannel()
    data class OnTeamCreate(val teamId: String) : TeamSetupChannel()

}

fun <T> List<T>.updateElement(predicate: (T) -> Boolean, transform: (T) -> T): List<T> {
    return map { if (predicate(it)) transform(it) else it }
}
