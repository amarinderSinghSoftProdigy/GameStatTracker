package com.softprodigy.ballerapp.ui.features.home.teams.manage_team

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

//@HiltViewModel
class ManageTeamRstrViewModel : ViewModel() {
    private val _manageTeamRstrState = mutableStateOf(ManageTeamRstrState())
    var manageTeamRstrState: State<ManageTeamRstrState> = _manageTeamRstrState
        private set

    init {
        // TODO: temp data for design
        _manageTeamRstrState.value = ManageTeamRstrState(
            teamData = mutableListOf(
                ManageTeamData(
                    id = "1111111111", position = "Coaches", players = mutableListOf(
                        TeamUser("22222", name = "Sam", role = "Coach", profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg"),
                        TeamUser("33333", name = "Michael", role = "Coach", profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg")
                    )
                ),
                ManageTeamData(
                    id = "444444", position = "Point Guards", players = mutableListOf(
                        TeamUser("55555", name = "Alfredo", role = "PG", points = "77", profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg"),
                        TeamUser("666666", name = "Lincoln", role = "PG", points = "15", profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg")
                    )
                ),
                ManageTeamData(
                    id = "7777", position = "Shooting Guards", players = mutableListOf(
                        TeamUser("8888", name = "Charlie", role = "SG", points = "21", profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg"),
                        TeamUser("9999", name = "Gustavo", role = "SG", points = "48", profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg")
                    )
                ),
                ManageTeamData(
                    id = "7770", position = "Small Forwards", players = mutableListOf(
                        TeamUser("8888", name = "John", role = "SG", points = "21", profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg"),
                        TeamUser("9999", name = "Cena", role = "SG", points = "48", profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg")
                    )
                ),
            )
        )
    }
}

sealed class ManageTeamRstrChannel {
    data class ShowToast(val message: String) : ManageTeamRstrChannel()
}