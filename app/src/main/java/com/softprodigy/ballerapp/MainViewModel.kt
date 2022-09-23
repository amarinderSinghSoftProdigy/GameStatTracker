package com.softprodigy.ballerapp

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    application: Application,
) :
    AndroidViewModel(application) {

    var state = mutableStateOf(MainState())
        private set

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnTopBarChanges -> {
                state.value =
                    state.value.copy(showAppBar = event.showAppBar, topBar = event.topBarData)
            }
            is MainEvent.OnColorChanges ->{
                state.value = state.value.copy(color = event.color)

            }
            is MainEvent.OnShowTopBar -> {
                state.value = state.value.copy(showAppBar = event.showAppBar)

            }
        }
    }

}