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
                invitationState.value = invitationState.value.copy(showDialog = true)
            }
            is InvitationEvent.OnDeclineCLick -> {
                invitationState.value = invitationState.value.copy(showDialog = true)
            }
            is InvitationEvent.OnDialogClick -> {
                invitationState.value = invitationState.value.copy(showDialog = event.showDialog)

            }
            is InvitationEvent.OnRoleClick -> {
                invitationState.value = invitationState.value.copy(selectedRole = event.role)

            }
        }
    }
}


