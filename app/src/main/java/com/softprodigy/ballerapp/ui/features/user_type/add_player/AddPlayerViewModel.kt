package com.softprodigy.ballerapp.ui.features.user_type.add_player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.softprodigy.ballerapp.data.request.GlobalRequest

//@HiltViewModel
class AddPlayerViewModel : ViewModel() {
    private val _selectedPlayer = mutableStateListOf<String>()
    var selectedPlayer = _selectedPlayer
        private set

    var teamData by mutableStateOf(GlobalRequest.AddPlayers())
        private set

    fun saveTeamData(request: GlobalRequest.AddPlayers) {
        teamData = request

    }
}