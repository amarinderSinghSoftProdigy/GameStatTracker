package com.softprodigy.ballerapp.ui.features.home.events.venues

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.softprodigy.ballerapp.data.response.division.DivisionData
import com.softprodigy.ballerapp.data.response.division.DivisionTabs
import com.softprodigy.ballerapp.data.response.division.VenuesData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VenuesViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    private val _venuesUiState = mutableStateOf(VenuesUIState())
    val venuesUiState: State<VenuesUIState> = _venuesUiState

    init {
        initDivision()
    }

    private fun initDivision() {
        val venuesData = ArrayList<VenuesData>()
        venuesData.add(
            VenuesData(
                "https://image.shutterstock.com/image-photo/word-demo-appearing-behind-torn-260nw-1782295403.jpg",
                "Karl Malone Training Center"
            )
        )
        venuesData.add(
            VenuesData(
                "https://image.shutterstock.com/image-photo/word-demo-appearing-behind-torn-260nw-1782295403.jpg",
                "Lehi Junior High"
            )
        )
        venuesData.add(
            VenuesData(
                "https://image.shutterstock.com/image-photo/word-demo-appearing-behind-torn-260nw-1782295403.jpg",
                "N Warehouse"
            )
        )
        venuesData.add(
            VenuesData(
                "https://image.shutterstock.com/image-photo/word-demo-appearing-behind-torn-260nw-1782295403.jpg",
                "Norton Performance Center"
            )
        )
        venuesData.add(
            VenuesData(
                "https://image.shutterstock.com/image-photo/word-demo-appearing-behind-torn-260nw-1782295403.jpg",
                "Treeside Charter School"
            )
        )

        _venuesUiState.value = _venuesUiState.value.copy(venuesData = venuesData)
    }

}