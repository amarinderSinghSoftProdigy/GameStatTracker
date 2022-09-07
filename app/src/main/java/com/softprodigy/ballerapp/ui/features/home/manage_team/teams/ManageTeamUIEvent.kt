package com.softprodigy.ballerapp.ui.features.home.manage_team.teams

sealed class ManageTeamUIEvent {

    data class OnColorSelected(val selectedColor: String) : ManageTeamUIEvent()
    data class OnTeamNameChange(val teamName: String) : ManageTeamUIEvent()
    data class OnImageSelected(val teamImageUri: String) : ManageTeamUIEvent()
}