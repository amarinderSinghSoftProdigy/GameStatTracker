package com.allballapp.android.ui.features.home.teams.manage_team

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.allballapp.android.data.response.team.Player

//@HiltViewModel
class ManageTeamRstrViewModel : ViewModel() {
    private val _manageTeamRstrState = mutableStateOf(ManageTeamRstrState())
    var manageTeamRstrState: State<ManageTeamRstrState> = _manageTeamRstrState
        private set

    init {
        // TODO: temp data for design
        _manageTeamRstrState.value = manageTeamRstrState.value.copy(
            teamData = mutableListOf(
                ManageTeamData(
                    id = "1111111111", position = "Coaches", players = mutableListOf(
                        TeamUser(
                            "22222",
                            name = "Sam",
                            role = "Coach",
                            profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg"
                        ),
                        TeamUser(
                            "33333",
                            name = "Michael",
                            role = "Coach",
                            profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg"
                        )
                    )
                ),
                ManageTeamData(
                    id = "444444", position = "Point Guards", players = mutableListOf(
                        TeamUser(
                            "55555",
                            name = "Alfredo",
                            role = "PG",
                            points = "77",
                            profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg"
                        ),
                        TeamUser(
                            "666666",
                            name = "Lincoln",
                            role = "PG",
                            points = "15",
                            profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg"
                        )
                    )
                ),
                ManageTeamData(
                    id = "7777", position = "Shooting Guards", players = mutableListOf(
                        TeamUser(
                            "8888",
                            name = "Charlie",
                            role = "SG",
                            points = "21",
                            profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg"
                        ),
                        TeamUser(
                            "9999",
                            name = "Gustavo",
                            role = "SG",
                            points = "48",
                            profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg"
                        )
                    )
                ),
                ManageTeamData(
                    id = "7770", position = "Small Forwards", players = mutableListOf(
                        TeamUser(
                            "8888",
                            name = "John",
                            role = "SG",
                            points = "21",
                            profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg"
                        ),
                        TeamUser(
                            "9999",
                            name = "Cena",
                            role = "SG",
                            points = "48",
                            profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg"
                        )
                    )
                ),
            )
        )

        _manageTeamRstrState.value = manageTeamRstrState.value.copy(
            allPlayer = mutableListOf(
                Player(
                    _id = "11111",
                    name = "George Will",
                    profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                    email = "email@gmail.com"
                ),
                Player(
                    _id = "11111",
                    name = "George Will",
                    profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                    email = "email@gmail.com"
                ),
                Player(
                    _id = "11111",
                    name = "George Will",
                    profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                    email = "email@gmail.com"
                ),
                Player(
                    _id = "11111",
                    name = "George Will",
                    profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                    email = "email@gmail.com"
                ),
                Player(
                    _id = "11111",
                    name = "George Will",
                    profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                    email = "email@gmail.com"
                ),
                Player(
                    _id = "11111",
                    name = "George Will",
                    profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                    email = "email@gmail.com"
                ),
                Player(
                    _id = "11111",
                    name = "George Will",
                    profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                    email = "email@gmail.com"
                ),
                Player(
                    _id = "11111",
                    name = "George Will",
                    profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                    email = "email@gmail.com"
                ),
            )
        )
    }

    fun onEvent(event: ManageTeamRstrUIEvent) {
        when (event) {
            is ManageTeamRstrUIEvent.OnDialogClick -> {
                _manageTeamRstrState.value =
                    _manageTeamRstrState.value.copy(showDialog = event.showDialog)
            }
            is ManageTeamRstrUIEvent.OnSearch -> {
                _manageTeamRstrState.value =
                    _manageTeamRstrState.value.copy(search = event.searchKey)
                searchPlayer(searchKey = event.searchKey)
            }

            is ManageTeamRstrUIEvent.OnPlayerClick -> {
                if (!_manageTeamRstrState.value.selectedPlayers.contains(event.player)) {
                    _manageTeamRstrState.value =
                        _manageTeamRstrState.value.copy(selectedPlayers = ((((_manageTeamRstrState.value.selectedPlayers) + event.player) as MutableList<Player>)))
                } else {
                    _manageTeamRstrState.value =
                        _manageTeamRstrState.value.copy(selectedPlayers = ((((_manageTeamRstrState.value.selectedPlayers) - event.player) as MutableList<Player>)))

                }
            }

        }
    }

    private fun searchPlayer(searchKey: String) {
        if (searchKey.length > 2) {
            _manageTeamRstrState.value = _manageTeamRstrState.value.copy(matchedPlayers =
            _manageTeamRstrState.value.allPlayer.filter {
                it.name.contains(searchKey, ignoreCase = true) || it.email!!.contains(
                    searchKey,
                    ignoreCase = true
                )
            } as MutableList<Player>
            )
        } else {
            _manageTeamRstrState.value =
                _manageTeamRstrState.value.copy(matchedPlayers = mutableListOf())
        }
    }

}

sealed class ManageTeamRstrChannel {
    data class ShowToast(val message: String) : ManageTeamRstrChannel()
}