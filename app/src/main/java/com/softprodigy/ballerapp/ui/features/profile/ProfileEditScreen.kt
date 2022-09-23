package com.softprodigy.ballerapp.ui.features.profile


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.CheckBoxData
import com.softprodigy.ballerapp.data.response.TeamDetails
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.theme.error

@Composable
fun ProfileEditScreen(
    vm: ProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state = vm.state.value

/*    var firstName by rememberSaveable { mutableStateOf("George") }
    var lastName by rememberSaveable { mutableStateOf("Will") }
    var email by rememberSaveable { mutableStateOf("email@example.com") }
    var phoneNo by rememberSaveable { mutableStateOf("(704) 555-0127") }
    var birthday by rememberSaveable { mutableStateOf("May 15, 1999") }
    var classOf by rememberSaveable { mutableStateOf("Class of") }

 */
//    var jerseyNo by rememberSaveable { mutableStateOf("23, 16, 18") }
//    var gender by rememberSaveable { mutableStateOf("Male") }
    var waistSize by rememberSaveable { mutableStateOf("32") }
    var shirtSize by rememberSaveable { mutableStateOf("Adult, L") }
    val favColgTeam by rememberSaveable { mutableStateOf("Team Name") }
    var favNbaTeam by rememberSaveable { mutableStateOf("Team Name") }
    var favActivePlayer by rememberSaveable { mutableStateOf("Team Name") }
    var favAllTIme by rememberSaveable { mutableStateOf("Team Name") }

    val listOfCheckbox = listOf(
        CheckBoxData(stringResource(id = R.string.pg)),
        CheckBoxData(stringResource(id = R.string.sg)),
        CheckBoxData(stringResource(id = R.string.sf)),
        CheckBoxData(stringResource(id = R.string.pf)),
        CheckBoxData(stringResource(id = R.string.c))
    )

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = dimensionResource(id = R.dimen.size_16dp)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))
                    Image(
                        painter = painterResource(id = R.drawable.user_demo),
                        contentDescription = "",
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_200dp))
                            .padding(bottom = dimensionResource(id = R.dimen.size_16dp))
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_48dp)))
                    DividerCommon()
                    EditProfileFields(
                        state.user.firstName,
                        onValueChange = {
                            vm.onEvent(ProfileEvent.OnFirstNameChange(it))
                        },
                        stringResource(id = R.string.first_name),
                        errorMessage = stringResource(id = R.string.valid_first_name),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                    )
                    DividerCommon()
                    EditProfileFields(
                        state.user.lastName,
                        onValueChange = {
                            vm.onEvent(ProfileEvent.OnLastNameChange(it))
                        },
                        stringResource(id = R.string.last_name),
                        errorMessage = stringResource(id = R.string.valid_last_name),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                    )
                    DividerCommon()
                    EditProfileFields(
                        state.user.email,
                        onValueChange = {
                            vm.onEvent(ProfileEvent.OnEmailChange(it))

                        },
                        stringResource(id = R.string.email),
                        errorMessage = stringResource(id = R.string.valid_last_name),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                    )
                    DividerCommon()
                    EditProfileFields(
                        state.user.phone,
                        onValueChange = {
                            vm.onEvent(ProfileEvent.OnPhoneChange(it))

                        },
                        stringResource(id = R.string.phone_num),
                        errorMessage = stringResource(id = R.string.valid_last_name),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                    )
                    DividerCommon()
                    EditProfileFields(
                        state.user.birthdate,
                        onValueChange = {
                            vm.onEvent(ProfileEvent.OnBirthdayChange(it))

                        },
                        stringResource(id = R.string.birthday),
                        errorMessage = stringResource(id = R.string.valid_last_name),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                    )
                    DividerCommon()

                    EditProfileFields(
                        state.user.userDetails.classOf,
                        onValueChange = {
                            vm.onEvent(ProfileEvent.OnClassChange(it))

                        },
                        stringResource(id = R.string.classof),
                        errorMessage = stringResource(id = R.string.valid_last_name),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                    )
                    DividerCommon()
                }
            }

            AppText(
                text = stringResource(id = R.string.positons),
                style = MaterialTheme.typography.h2,
                color = ColorBWBlack,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.size_16dp))
                    .fillMaxWidth()
            )

            UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
                Row(modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp))) {
                    listOfCheckbox.forEachIndexed { index, item ->
                        CheckBoxItem(item = item)
                    }
                }
            }

            AppText(
                text = stringResource(id = R.string.teams_label),
                style = MaterialTheme.typography.h2,
                color = ColorBWBlack,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.size_16dp))
                    .fillMaxWidth(),
            )
            UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
                Teams(
                    teams = state.user.teamDetails,
                    onLeaveTeamClick = { index ->
                        vm.onEvent(ProfileEvent.OnLeaveTeamCLick(index))
                    },
                    onPositionChange = { index, position ->
                        vm.onEvent(ProfileEvent.OnPositionChange(index, position))
                    },
                    onRoleChange = { index, role ->
                        vm.onEvent(ProfileEvent.OnRoleChange(index, role))
                    },
                    onJerseyNumberChange = { index, number ->
                        vm.onEvent(ProfileEvent.OnJerseyChange(index, number))
                    }
                )
            }

            AppText(
                text = stringResource(id = R.string.jersey_pref),
                style = MaterialTheme.typography.h2,
                color = ColorBWBlack,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.size_16dp))
                    .fillMaxWidth()
            )
            UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
                EditProfileFields(
                    data =
                   /* if (state.user.userDetails.jerseyPerferences.isNotEmpty()) {
                        *//*It will convert array of string to comma sep String*//*
                        state.user.userDetails.jerseyPerferences[0].jerseyNumberPerferences.joinToString { number -> number }
                    } else
                        ""*/

                    state.jerseyPerferences[0].jerseyNumberPerferences.joinToString { number -> number }
                    ,
                    onValueChange = {
                        /*It will convert comma sep String to array*/
//                        jerseyNo = it
                        val commaSepJersey = it.split(",").toMutableStateList()

                      vm.onEvent(ProfileEvent.OnPrefJerseyNoChange(commaSepJersey))
                    },
                    stringResource(id = R.string.jersey_number),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )
                DividerCommon()

                EditProfileFields(
                    state.user.gender,
                    onValueChange = {
                        vm.onEvent(ProfileEvent.OnGenderChange(it))

                    },
                    stringResource(id = R.string.gender),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )
                DividerCommon()

                EditProfileFields(
                    state.jerseyPerferences[0].shirtSize,
                    onValueChange = {
//                        shirtSize = it
                        vm.onEvent(ProfileEvent.OnShirtChange(it))

                    },
                    stringResource(id = R.string.shirt_size),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )
                DividerCommon()

                EditProfileFields(
                    waistSize,
                    onValueChange = {
                        waistSize = it
                    },
                    stringResource(id = R.string.waist_size),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )
            }
            AppText(
                text = stringResource(id = R.string.fun_facts),
                style = MaterialTheme.typography.h2,
                color = ColorBWBlack,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.size_16dp))
                    .fillMaxWidth(),
            )
            UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {


                EditProfileFields(
                    favColgTeam,
                    onValueChange = {
                        favNbaTeam = it
                    },
                    stringResource(id = R.string.favorite_college_team),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )
                DividerCommon()

                EditProfileFields(
                    favNbaTeam,
                    onValueChange = {
                        favNbaTeam = it
                    },
                    stringResource(id = R.string.favorite_nba_team),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )
                DividerCommon()

                EditProfileFields(
                    favActivePlayer,
                    onValueChange = {
                        favActivePlayer = it
                    },
                    stringResource(id = R.string.favorite_active_player),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )
                DividerCommon()

                EditProfileFields(
                    favAllTIme,
                    onValueChange = {
                        favAllTIme = it
                    },
                    stringResource(id = R.string.favoritea_all_time_tlayer),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )
            }

            AppButton(
                enabled = true,
                icon = null,
                themed = true,
                onClick = { },
                text = stringResource(id = R.string.save),
                isForceEnableNeeded = true
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
        }

        if (state.isLoading) {
            CommonProgressBar()
        }
    }

}


@Composable
fun Teams(
    teams: SnapshotStateList<TeamDetails>,
    onLeaveTeamClick: (Int) -> Unit,
    onPositionChange: (Int, String) -> Unit,
    onRoleChange: (Int, String) -> Unit,
    onJerseyNumberChange: (Int, String) -> Unit,
) {
//    var role by rememberSaveable { mutableStateOf("Player") }
//    var position by rememberSaveable { mutableStateOf("PG") }
//    var jerseyNo by rememberSaveable { mutableStateOf("17") }

    /*  val teams: List<Team> = listOf<Team>(
          Team(name = "Springfield Bucks", role = "Player"),
      )*/


    teams.forEachIndexed { index, teamDetails ->
        Column(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.appColors.material.primary)
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp))
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        /*  Image(
                              painter = painterResource(id = R.drawable.user_demo),
                              contentDescription = "",
                              modifier = Modifier
                                  .size(dimensionResource(id = R.dimen.size_24dp))
                                  .clip(CircleShape)
                          )*/
                        CoilImage(
                            src = BuildConfig.IMAGE_SERVER + teamDetails.teamId,
//                        contentDescription = "",
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_24dp))
                                .clip(CircleShape),
                            isCrossFadeEnabled = false,
                            onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                            onError = { Placeholder(R.drawable.ic_team_placeholder) }
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
                        AppText(
                            text = teamDetails.teamId.name,
                            style = MaterialTheme.typography.h5,
                            color = ColorBWBlack
                        )
                    }
                    ClickableText(
                        style = TextStyle(color = error),
                        text = AnnotatedString(stringResource(id = R.string.leave_team)),
                        onClick = {

                        })
                }
                DividerCommon()

                EditProfileFields(
                    teamDetails.role,
                    onValueChange = {
                        onRoleChange.invoke(index, it)

                    },
                    stringResource(id = R.string.role),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )
                DividerCommon()

                EditProfileFields(
                    teamDetails.position,
                    onValueChange = {
                        onPositionChange.invoke(index, it)
                    },
                    stringResource(id = R.string.position),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )
                DividerCommon()

                EditProfileFields(
                    teamDetails.jersey,
                    onValueChange = {
                        if (it.length <= 3)
                            onJerseyNumberChange.invoke(index, it)
                    },
                    stringResource(id = R.string.jersey_number),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Number,
                    ),
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

        }
    }

}

@Composable
fun CheckBoxItem(item: CheckBoxData) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(
            end = dimensionResource(id = R.dimen.size_12dp),
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clickable {
                    item.isChecked = !item.isChecked
                }
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)))
                .size(
                    dimensionResource(id = R.dimen.size_16dp)
                )
                .background(
                    color =
                    if (item.isChecked) {
                        MaterialTheme.appColors.material.primaryVariant
                    } else Color.White
                )
                .border(
                    width =
                    if (item.isChecked) {
                        0.dp
                    } else dimensionResource(id = R.dimen.size_1dp),
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)),
                    color = if (item.isChecked)
                        Color.Transparent
                    else
                        MaterialTheme.appColors.buttonColor.bckgroundDisabled
                )
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
        AppText(
            text = stringResource(id = R.string.pg),
            style = MaterialTheme.typography.body1,
            color = ColorBWBlack,
        )
    }
}

