package com.softprodigy.ballerapp.ui.features.user_type

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.softprodigy.ballerapp.data.request.GlobalRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserTypeViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    var userRole by mutableStateOf("")
        private set


    var teamData by mutableStateOf(GlobalRequest.SetUpTeam())
        private set

    fun saveData(request: GlobalRequest.Users) {
        userRole = request.role
    }


    fun saveTeamData(request: GlobalRequest.SetUpTeam) {
        teamData = request

    }
}