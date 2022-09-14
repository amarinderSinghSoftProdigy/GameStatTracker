package com.softprodigy.ballerapp.ui.features.home.invitation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class InvitationViewModel : ViewModel() {
    var invitationState = mutableStateOf(InvitationState())
        private set

    init {
        invitationState.value = invitationState.value.copy(
            invitations =
            arrayListOf(
                InvitationDemoModel(
                    id = "111",
                    title = "Invitation Title",
                    sentBy = "George Will",
                    logo = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                    time = "Fri, May 20",
                    status = "pending"
                ),
                InvitationDemoModel(
                    id = "222",
                    title = "Invitation Title",
                    sentBy = "George Will",
                    logo = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                    time = "Fri, May 20",
                    status = "accepted"
                ),
                InvitationDemoModel(
                    id = "111",
                    title = "Invitation Title",
                    sentBy = "George Will",
                    logo = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                    time = "Fri, May 20",
                    status = "declined"
                ),
            )
        )
    }

    fun onEvent(event: InvitationEvent) {
        when (event) {
            is InvitationEvent.OnAcceptCLick -> {
                invitationState.value = invitationState.value.copy(showRoleDialog = true)
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
                    showDeclineDialog = false,invitations = (((invitationState.value.invitations) - event.invitation) as ArrayList<InvitationDemoModel>)
                )
            }
            is InvitationEvent.OnDeleteDialogClick -> {
                invitationState.value =
                    invitationState.value.copy(showDeclineDialog = event.showDeleteDialog)

            }

        }
    }
    }



