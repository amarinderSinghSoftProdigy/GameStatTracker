package com.softprodigy.ballerapp.ui.features.home.invitation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
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
            is InvitationEvent.OnAcceptCLick -> {
                invitationState.value = invitationState.value.copy(
                    showRoleDialog = true,
                    selectedInvitation = event.invitation,
                    teamId = event.invitation.team._id
                )
            }
            is InvitationEvent.OnDeclineCLick -> {
                invitationState.value = invitationState.value.copy(
                    showDeclineDialog = true,
                    selectedInvitation = event.invitation
                )

            }
            is InvitationEvent.OnRoleDialogClick -> {
                invitationState.value =
                    invitationState.value.copy(showRoleDialog = event.showRoleDialog)

            }
            is InvitationEvent.OnRoleClick -> {
                invitationState.value = invitationState.value.copy(selectedRole = event.role)

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
                        role = invitationState.value.selectedRole,
                        playerGender = invitationState.value.selectedGender,
                        playerId = invitationState.value.selectedPlayerId
                    )
                    invitationState.value = invitationState.value.copy(selectedRole = "")
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
                _invitationChannel.send(
                    InvitationChannel.ShowToast(
                        UiText.DynamicString(
                            inviteResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                inviteResponse.value.let { response ->
                    if (response.status) {
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
        playerGender: String
    ) {
        Timber.i("acceptTeamInvitation-- id--$invitationId role--$role")
        invitationState.value =
            invitationState.value.copy(showLoading = true)
        val acceptInviteResponse =
            teamRepo.acceptTeamInvitation(
                invitationId = invitationId,
                role = role,
                playerId = playerId,
                playerGender = playerGender
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
                _invitationChannel.send(
                    InvitationChannel.ShowToast(
                        UiText.DynamicString(
                            acceptInviteResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                acceptInviteResponse.value.let { response ->
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
                _invitationChannel.send(
                    InvitationChannel.ShowToast(
                        UiText.DynamicString(
                            rejectInviteResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                rejectInviteResponse.value.let { response ->
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

    private suspend fun getUserRoles() {
        invitationState.value = invitationState.value.copy(showLoading = true)

        when (val userRoles = teamRepo.getUserRoles()) {
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

                _invitationChannel.send(
                    InvitationChannel.ShowToast(
                        UiText.DynamicString(
                            userRoles.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                userRoles.value.let { response ->
                    if (response.status) {
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

        when (val userRoles = teamRepo.getPlayerById(invitationState.value.teamId)) {
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

                _invitationChannel.send(
                    InvitationChannel.ShowToast(
                        UiText.DynamicString(
                            userRoles.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                userRoles.value.let { response ->
                    if (response.status) {
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
