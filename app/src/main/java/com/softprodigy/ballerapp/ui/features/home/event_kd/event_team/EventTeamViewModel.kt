package com.softprodigy.ballerapp.ui.features.home.event_kd.event_team

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.response.team.Team
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventTeamViewModel @Inject constructor(dataStoreManager: DataStoreManager) : ViewModel() {
    var eventTeamState = mutableStateOf(EventTeamState())
        private set

    init {
        eventTeamState.value = eventTeamState.value.copy(
            eventTeams = listOf(
                EventTeamModel(
                    "Boys 3rd",
                    teams = listOf(
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),

                        )
                ),
                EventTeamModel(
                    "Boys 7th",
                    teams = listOf(
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),

                        )
                ),

                EventTeamModel(
                    "Boys 7th",
                    teams = listOf(
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),

                        )
                ),

                EventTeamModel(
                    "Boys 7th",
                    teams = listOf(
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),
                        Team(
                            name = "Team Name",
                            logo = "teamLogo/1663573691074-IMG_20220805_120020_710.jpg"
                        ),

                        )
                ),

            )
        )
    }
}