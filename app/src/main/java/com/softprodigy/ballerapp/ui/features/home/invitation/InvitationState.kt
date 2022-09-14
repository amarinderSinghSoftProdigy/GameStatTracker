package com.softprodigy.ballerapp.ui.features.home.invitation





data class InvitationState(
    val invitations: ArrayList<Invitation> = arrayListOf(),
    val roles: ArrayList<String> = arrayListOf("Project Manager", "Coach", "Assistance Coach", "Player", "Guardian"),
    val showRoleDialog: Boolean = false,
    val showDeclineDialog: Boolean = false,
    val showLoading: Boolean = false,
    val selectedRole:String="",
    val selectedInvitation:Invitation=Invitation(),

    )