package com.softprodigy.ballerapp.ui.features.home.invitation

import com.softprodigy.ballerapp.data.response.PlayerDetails
import com.softprodigy.ballerapp.data.response.invitation.UserRoleModel

data class InvitationState(
    val invitations: ArrayList<Invitation> = arrayListOf(),
    val roles: ArrayList<UserRoleModel> = arrayListOf(),
    val showRoleDialog: Boolean = false,
    val showDeclineDialog: Boolean = false,
    val showLoading: Boolean = false,
    val selectedRole: UserRoleModel = UserRoleModel(),
    val selectedInvitation: Invitation = Invitation(),
    val showGuardianDialog: Boolean = false,
    val showAddPlayerDialog: Boolean = false,
    val selectedGuardian: String = "",
    val teamId: String = "",
    val playerDetails: ArrayList<PlayerDetails> = arrayListOf(),
    val selectedPlayerId: String = "",
    val selectedGender: String = ""
)