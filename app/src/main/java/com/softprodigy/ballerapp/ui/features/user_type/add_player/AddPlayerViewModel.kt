package com.softprodigy.ballerapp.ui.features.user_type.add_player

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

//@HiltViewModel
class AddPlayerViewModel : ViewModel() {
    private val _selectedPlayer = mutableStateListOf<String>()
    var selectedPlayer = _selectedPlayer
        private set

}