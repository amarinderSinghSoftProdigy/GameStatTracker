package com.softprodigy.ballerapp.ui.features.home.event_kd.schedule

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.response.team.Team
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventScheduleViewModel @Inject constructor(dataStoreManager: DataStoreManager) : ViewModel() {
    var eventScheduleState = mutableStateOf(EventScheduleState())
        private set

    init {
        eventScheduleState.value = eventScheduleState.value.copy(
            leagueSchedules = listOf(
                LeagueScheduleModel(
                    "Sep 1, 2022", "2", matches = listOf(
                        Match(
                            teamA = Team(
                                name = "Titans",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            teamB = Team(
                                name = "Champions",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            location = "Chicago,IL",
                            divisions = "Boys 8th",
                            time = "5:00 PM"
                        ), Match(
                            teamA = Team(
                                name = "Royals",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            teamB = Team(
                                name = "Lions",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            location = "Chicago,IL",
                            divisions = "Boys 8th",
                            time = "5:00 PM"
                        )
                    )
                ),
                LeagueScheduleModel(
                    "Sep 3, 2022", "3", matches = listOf(
                        Match(
                            teamA = Team(
                                name = "Titans",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            teamB = Team(
                                name = "Champions",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            location = "Chicago,IL",
                            divisions = "Boys 8th",
                            time = "5:00 PM"
                        ), Match(
                            teamA = Team(
                                name = "Royals",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            teamB = Team(
                                name = "Lions",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            location = "Chicago,IL",
                            divisions = "Boys 8th",
                            time = "5:00 PM"
                        ), Match(
                            teamA = Team(
                                name = "Kings",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            teamB = Team(
                                name = "Warriors",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            location = "Chicago,IL",
                            divisions = "Boys 8th",
                            time = "5:00 PM"
                        )
                    )
                ),
                LeagueScheduleModel(
                    "Sep 4, 2022", "2", matches = listOf(
                        Match(
                            teamA = Team(
                                name = "Titans",
                                logo = "teamLogo/1662462524256-IMG_20220805_120020_710.jpg"
                            ),
                            teamB = Team(
                                name = "Champions",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            location = "Chicago,IL",
                            divisions = "Boys 8th",
                            time = "5:00 PM"
                        ), Match(
                            teamA = Team(
                                name = "Royals",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            teamB = Team(
                                name = "Lions",
                                logo = "profileImage/1662462390526-IMG_20220805_120020_710.jpg"
                            ),
                            location = "Chicago,IL",
                            divisions = "Boys 8th",
                            time = "5:00 PM"
                        )
                    )
                )
            )
        )
    }
}