package com.softprodigy.ballerapp.ui.features.profile


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.CheckBoxData
import com.softprodigy.ballerapp.data.response.Team
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.DividerCommon
import com.softprodigy.ballerapp.ui.features.components.EditProfileFields
import com.softprodigy.ballerapp.ui.features.components.UserFlowBackground
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.error

@Composable
fun ProfileEditScreen(
    onBackClick: () -> Unit
) {
    var firstName by rememberSaveable { mutableStateOf("George") }
    var lastName by rememberSaveable { mutableStateOf("Will") }
    var email by rememberSaveable { mutableStateOf("email@example.com") }
    var phoneNo by rememberSaveable { mutableStateOf("(704) 555-0127") }
    var birthday by rememberSaveable { mutableStateOf("May 15, 1999") }
    var classOf by rememberSaveable { mutableStateOf("Class of") }
    var jerseyNo by rememberSaveable { mutableStateOf("23, 16, 18") }
    var gender by rememberSaveable { mutableStateOf("Male") }
    var waistSize by rememberSaveable { mutableStateOf("32") }
    var shirtSize by rememberSaveable { mutableStateOf("Adult, L") }

    var favColgTeam by rememberSaveable { mutableStateOf("Team Name") }
    var favNbaTeam by rememberSaveable { mutableStateOf("Team Name") }
    var favActivePlayer by rememberSaveable { mutableStateOf("Team Name") }
    var favAllTIme by rememberSaveable { mutableStateOf("Team Name") }

    var listOfCheckbox = listOf(
        CheckBoxData(stringResource(id = R.string.pg)),
        CheckBoxData(stringResource(id = R.string.sg)),
        CheckBoxData(stringResource(id = R.string.sf)),
        CheckBoxData(stringResource(id = R.string.pf)),
        CheckBoxData(stringResource(id = R.string.c))
    )

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
                Image(
                    painter = painterResource(id = R.drawable.user_demo),
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.size_200dp))
                        .padding(bottom = dimensionResource(id = R.dimen.size_16dp))
                        .clip(CircleShape)
                )
                DividerCommon()
                EditProfileFields(
                    firstName,
                    onValueChange = {
                        firstName = it
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
                    lastName,
                    onValueChange = {
                        lastName = it
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
                    email,
                    onValueChange = {
                        email = it
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
                    phoneNo,
                    onValueChange = {
                        phoneNo = it
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
                    birthday,
                    onValueChange = {
                        birthday = it
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
                    classOf,
                    onValueChange = {
                        classOf = it
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
            style = MaterialTheme.typography.h6,
            color = ColorBWBlack,
            fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.size_16dp))
                .fillMaxWidth()
        )

        UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
            Row {
                listOfCheckbox.forEachIndexed { index, item ->
                    CheckBoxItem(item = item)
                }
            }
        }

        AppText(
            text = stringResource(id = R.string.teams_label),
            style = MaterialTheme.typography.h6,
            color = ColorBWBlack,
            fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.size_16dp))
                .fillMaxWidth(),
        )
        UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
            Teams()
        }

        AppText(
            text = stringResource(id = R.string.jersey_pref),
            style = MaterialTheme.typography.h6,
            color = ColorBWBlack,
            fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.size_16dp))
                .fillMaxWidth()
        )
        UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
            EditProfileFields(
                jerseyNo,
                onValueChange = {
                    jerseyNo = it
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
                gender,
                onValueChange = {
                    gender = it
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
                shirtSize,
                onValueChange = {
                    shirtSize = it
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
            style = MaterialTheme.typography.h6,
            color = ColorBWBlack,
            fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
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
}


@Composable
fun Teams() {
    var role by rememberSaveable { mutableStateOf("Player") }
    var position by rememberSaveable { mutableStateOf("PG") }
    var jerseyNo by rememberSaveable { mutableStateOf("17") }

    var teams: List<Team> = listOf<Team>(
        Team(name = "Springfield Bucks", role = "Player"),
    )
    teams.forEach {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxSize()
                .background(color = Color.White)
                .padding(
                    top = dimensionResource(id = R.dimen.size_10dp),
                    bottom = dimensionResource(id = R.dimen.size_10dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        start = dimensionResource(id = R.dimen.size_10dp),
                        end = dimensionResource(id = R.dimen.size_10dp),
                        bottom = dimensionResource(id = R.dimen.size_10dp)
                    )
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    Image(
                        painter = painterResource(id = R.drawable.user_demo),
                        contentDescription = "",
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_20dp))
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
                    AppText(
                        text = it.name,
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
                role,
                onValueChange = {
                    role = it
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
                position,
                onValueChange = {
                    position = it
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
                jerseyNo,
                onValueChange = {
                    jerseyNo = it
                },
                stringResource(id = R.string.jersey_number),
                errorMessage = stringResource(id = R.string.valid_first_name),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences
                ),
            )
        }
    }
}

@Composable
fun CheckBoxItem(item: CheckBoxData) {
    Row {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = { item.isChecked = it },
        )
        AppText(
            text = stringResource(id = R.string.pg),
            style = MaterialTheme.typography.h6,
            color = ColorBWBlack,
        )
    }
}

