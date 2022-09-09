package com.softprodigy.ballerapp.ui.features.select_profile

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.softprodigy.ballerapp.data.response.SelectProfileResponse
import com.softprodigy.ballerapp.ui.features.home.manage_team.leaderboard.ManageLeaderBoardUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectProfileViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    private val _selectProfileUiState = mutableStateOf(SelectProfileUIState())
    val selectProfileUiState: State<SelectProfileUIState> = _selectProfileUiState

    fun onEvent(event: SelectProfileUIEvent) {
        when (event) {
            is SelectProfileUIEvent.IsSelectedRole -> {

                _selectProfileUiState.value =
                    _selectProfileUiState.value.copy(isSelectedRole = event.role)
            }
        }
    }

    init {
        getProfileData()
    }

    private fun getProfileData() {

        val profileList = ArrayList<SelectProfileResponse>()
        profileList.add(SelectProfileResponse(name = "George Will", role = "Player"))
        profileList.add(SelectProfileResponse(name = "James Will", role = "Parent"))
        _selectProfileUiState.value =
            _selectProfileUiState.value.copy(selectProfileList = profileList)
    }
}