package com.allballapp.android.ui.features.home.invitation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.ui.utils.UiText
import com.allballapp.android.domain.repository.ITeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InvitationViewModel @Inject constructor(val teamRepo: ITeamRepository) : ViewModel() {
    var invitationState = mutableStateOf(InvitationState())
        private set

    private val _invitationChannel = Channel<InvitationChannel>()
    val invitationChannel = _invitationChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            getAllInvitation()
        }
    }

    fun onEvent(event: InvitationEvent) {
        when (event) {
            is InvitationEvent.SetTeamId -> {
                invitationState.value = invitationState.value.copy(teamId = event.teamId)
            }
            is InvitationEvent.SetData -> {
                invitationState.value = invitationState.value.copy(
                    selectedPlayerName = event.playerName,
                    selectedLogo = event.logo
                )
            }
            is InvitationEvent.OnAcceptCLick -> {
                invitationState.value = invitationState.value.copy(
                    showRoleDialog = true,
                    selectedInvitation = event.invitation,
                    teamId = event.invitation.team._id,
                    showDeclineDialog = false,
                )
            }
            is InvitationEvent.OnDeclineCLick -> {
                invitationState.value = invitationState.value.copy(
                    showDeclineDialog = true,
                    selectedInvitation = event.invitation,
                    showRoleDialog = false,
                )

            }
            is InvitationEvent.OnRoleDialogClick -> {
                invitationState.value =
                    invitationState.value.copy(showRoleDialog = event.showRoleDialog)

            }
            is InvitationEvent.OnRoleClick -> {
                invitationState.value = invitationState.value.copy(selectedRoleKey = event.roleKey)

            }
            is InvitationEvent.OnDeclineConfirmClick -> {
                invitationState.value = invitationState.value.copy(
                    showDeclineDialog = false,
                    invitations = (((invitationState.value.invitations) - event.invitation) as ArrayList<Invitation>)
                )
                viewModelScope.launch {
                    rejectInvitation(event.invitation.id)
                }
            }
            is InvitationEvent.OnDeleteDialogClick -> {
                invitationState.value =
                    invitationState.value.copy(showDeclineDialog = event.showDeleteDialog)

            }
            is InvitationEvent.OnRoleConfirmClick -> {
                viewModelScope.launch {
                    getPlayerById()
                }
            }

            is InvitationEvent.OnGuardianClick -> {
                invitationState.value =
                    invitationState.value.copy(selectedGuardian = event.guardian)
            }

            is InvitationEvent.OnGuardianDialogClick -> {
                invitationState.value =
                    invitationState.value.copy(showGuardianDialog = event.showGuardianDialog)
            }
            is InvitationEvent.OnAddPlayerDialogClick -> {
                invitationState.value =
                    invitationState.value.copy(showAddPlayerDialog = event.showAddPlayerDialog)
            }
            is InvitationEvent.OnPlayerAddedSuccessDialog -> {
                invitationState.value =
                    invitationState.value.copy(showPlayerAddedSuccessDialog = event.showPlayerAddedDialog)
            }

            InvitationEvent.OnClearGuardianValues -> {
                invitationState.value =
                    invitationState.value.copy(selectedGuardian = "")
            }

            InvitationEvent.OnClearValues -> {
            }

            is InvitationEvent.OnValuesSelected -> {
                invitationState.value = invitationState.value.copy(
                    selectedPlayerId = event.playerDetails.memberDetails!!.id,
                    selectedGender = event.playerDetails.memberDetails.gender
                )
            }

            is InvitationEvent.OnInvitationConfirm -> {
                viewModelScope.launch {
                    acceptTeamInvitation(
                        invitationId = invitationState.value.selectedInvitation.id,
                        role = invitationState.value.selectedRoleKey,
                        guardianGender = event.gender ?: "",
                        playerId = invitationState.value.selectedPlayerId
                    )
                    invitationState.value = invitationState.value.copy(selectedRoleKey = "")
                }
            }
            is InvitationEvent.ConfirmGuardianWithoutChildAlert -> {
                invitationState.value =
                    invitationState.value.copy(showGuardianOnlyConfirmDialog = event.showConfirmDialog)
            }

            is InvitationEvent.GetRoles -> {
                viewModelScope.launch {
                    getUserRoles()
                }
            }
        }
    }

    suspend fun getAllInvitation() {
        invitationState.value =
            invitationState.value.copy(showLoading = true)
        val inviteResponse = teamRepo.getAllInvitation()
        invitationState.value =
            invitationState.value.copy(showLoading = false)

        when (inviteResponse) {
            is ResultWrapper.GenericError -> {
                _invitationChannel.send(
                    InvitationChannel.ShowToast(
                        UiText.DynamicString(
                            "${inviteResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
               /* _invitationChannel.send(
                    InvitationChannel.ShowToast(
                        UiText.DynamicString(
                            inviteResponse.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                inviteResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        invitationState.value =
                            invitationState.value.copy(
                                invitations = response.data
                            )
                        getUserRoles()
                    } else {
                        _invitationChannel.send(
                            InvitationChannel.ShowToast(
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

    private suspend fun acceptTeamInvitation(
        invitationId: String,
        role: String,
        playerId: String,
        guardianGender: String
    ) {
        Timber.i("acceptTeamInvitation-- id--$invitationId role--$role playerId--$playerId guardianGender--$guardianGender")
        invitationState.value =
            invitationState.value.copy(showLoading = true)
        val acceptInviteResponse =
            teamRepo.acceptTeamInvitation(
                invitationId = invitationId,
                role = role,
                playerId = playerId,
                guardianGender = guardianGender.capitalize()
            )
        invitationState.value =
            invitationState.value.copy(showLoading = false)

        when (acceptInviteResponse) {
            is ResultWrapper.GenericError -> {
                _invitationChannel.send(
                    InvitationChannel.ShowToast(
                        UiText.DynamicString(
                            "${acceptInviteResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
               /* _invitationChannel.send(
                    InvitationChannel.ShowToast(
                        UiText.DynamicString(
                            acceptInviteResponse.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                acceptInviteResponse.value.let { response ->
                    getAllInvitation()
                    _invitationChannel.send(
                        InvitationChannel.Success(
                            UiText.DynamicString(
                                response.statusMessage
                            )
                        )
                    )
                }

            }
        }
    }

    private suspend fun rejectInvitation(invitationId: String) {
        Timber.i("rejectInvitation-- id--$invitationId ")

        invitationState.value =
            invitationState.value.copy(showLoading = true)
        val rejectInviteResponse =
            teamRepo.rejectTeamInvitation(invitationId = invitationId)
        invitationState.value =
            invitationState.value.copy(showLoading = false)

        when (rejectInviteResponse) {
            is ResultWrapper.GenericError -> {
                _invitationChannel.send(
                    InvitationChannel.ShowToast(
                        UiText.DynamicString(
                            "${rejectInviteResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
               /* _invitationChannel.send(
                    InvitationChannel.ShowToast(
                        UiText.DynamicString(
                            rejectInviteResponse.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                rejectInviteResponse.value.let { response ->
                    getAllInvitation()
                }
            }
        }
    }

    suspend fun getUserRoles() {
        invitationState.value = invitationState.value.copy(showLoading = true)

        when (val userRoles = teamRepo.getUserRoles("")) {
            is ResultWrapper.GenericError -> {
                invitationState.value = invitationState.value.copy(showLoading = false)

                _invitationChannel.send(
                    InvitationChannel.ShowToast(
                        UiText.DynamicString(
                            "${userRoles.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                invitationState.value = invitationState.value.copy(showLoading = false)

                /*_invitationChannel.send(
                    InvitationChannel.ShowToast(
                        UiText.DynamicString(
                            userRoles.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                userRoles.value.let { response ->
                    if (response.status && response.data != null) {
                        invitationState.value =
                            invitationState.value.copy(
                                showLoading = false,
                                roles = response.data
                            )
                    } else {

                        invitationState.value =
                            invitationState.value.copy(
                                showLoading = false,
                            )

                        _invitationChannel.send(
                            InvitationChannel.ShowToast(
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

    private suspend fun getPlayerById() {
        invitationState.value = invitationState.value.copy(showLoading = true)

        when (val userRoles = teamRepo.getPlayerById(invitationState.value.teamId, "")) {
            is ResultWrapper.GenericError -> {
                invitationState.value = invitationState.value.copy(showLoading = false)

                _invitationChannel.send(
                    InvitationChannel.ShowToast(
                        UiText.DynamicString(
                            "${userRoles.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                invitationState.value = invitationState.value.copy(showLoading = false)

               /* _invitationChannel.send(
                    InvitationChannel.ShowToast(
                        UiText.DynamicString(
                            userRoles.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                userRoles.value.let { response ->
                    if (response.status && response.data != null) {
                        invitationState.value =
                            invitationState.value.copy(
                                showLoading = false,
                                playerDetails = response.data
                            )
                    } else {

                        invitationState.value =
                            invitationState.value.copy(
                                showLoading = false,
                            )

                        _invitationChannel.send(
                            InvitationChannel.ShowToast(
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


sealed class InvitationChannel {
    data class ShowToast(val message: UiText) : InvitationChannel()
    data class Success(val message: UiText) : InvitationChannel()
}
