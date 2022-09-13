package com.softprodigy.ballerapp.ui.features.profile


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.Team
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.theme.*

@OptIn(ExperimentalComposeUiApi::class)
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
    var isPgChecked by rememberSaveable { mutableStateOf(false) }
    var isSgChecked by rememberSaveable { mutableStateOf(false) }
    var isSfChecked by rememberSaveable { mutableStateOf(false) }
    var isPfChecked by rememberSaveable { mutableStateOf(false) }
    var isCChecked by rememberSaveable { mutableStateOf(false) }

    var jerseyNo by rememberSaveable { mutableStateOf("23, 16, 18") }
    var gender by rememberSaveable { mutableStateOf("Male") }
    var waistSize by rememberSaveable { mutableStateOf("32") }
    var shirtSize by rememberSaveable { mutableStateOf("Adult, L") }

    var favColgTeam by rememberSaveable { mutableStateOf("Team Name") }
    var favNbaTeam by rememberSaveable { mutableStateOf("Team Name") }
    var favActivePlayer by rememberSaveable { mutableStateOf("Team Name") }
    var favAllTIme by rememberSaveable { mutableStateOf("Team Name") }

    Box(
        modifier = Modifier
            .background(color = MaterialTheme.appColors.material.primary)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(backgroundColor = ColorMainPrimary) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))
                        Image(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "",
                            modifier = Modifier
                                .clickable {
                                    onBackClick()
                                },
                        )
                        Box(
                            modifier = Modifier.weight(1f),
                        )
                        {
                            AppText(
                                text = stringResource(R.string.edit_profile),
                                style = MaterialTheme.typography.h6,
                                color = heading2OnDarkColor,
                                fontSize = dimensionResource(id = R.dimen.txt_size_20).value.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.appColors.material.primary)
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxSize()
                            .padding(all = dimensionResource(id = R.dimen.size_10dp))
                            .background(color = Color.White)
                            .padding(top = dimensionResource(id = R.dimen.size_10dp)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.user_demo),
                            contentDescription = "",
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_200dp))
                                .padding(bottom = dimensionResource(id = R.dimen.size_10dp))
                                .clip(CircleShape)
                        )
                        HorizontalLine()
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
                        HorizontalLine()
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
                        HorizontalLine()
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
                        HorizontalLine()
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
                        HorizontalLine()
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
                        HorizontalLine()
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
                        HorizontalLine()
                    }

                    AppText(
                        text = stringResource(id = R.string.positons),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                        fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                        modifier = Modifier
                            .padding(
                                start = dimensionResource(id = R.dimen.size_10dp),
                                end = dimensionResource(id = R.dimen.size_10dp)
                            ),
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                all = dimensionResource(id = R.dimen.size_10dp),
                            )
                            .clip(RoundedCornerShape(5.dp))
                            .background(color = Color.White)

//                            .padding(top = dimensionResource(id = R.dimen.size_10dp)),
                    ) {
                        Checkbox(
                            checked = isPgChecked,
                            onCheckedChange = { isPgChecked = it },
                        )
                        AppText(
                            text = stringResource(id = R.string.pg),
                            style = MaterialTheme.typography.h6,
                            color = ColorBWBlack,
                        )
                        Checkbox(
                            checked = isSgChecked,
                            modifier = Modifier.padding(start = 2.dp),
                            onCheckedChange = { isSgChecked = it },
                        )
                        AppText(
                            text = stringResource(id = R.string.sg),
                            style = MaterialTheme.typography.h6,
                            color = ColorBWBlack,
                        )
                        Checkbox(
                            checked = isSfChecked,
                            modifier = Modifier.padding(start = 2.dp),
                            onCheckedChange = { isSfChecked = it },
                        )
                        AppText(
                            text = stringResource(id = R.string.sf),
                            style = MaterialTheme.typography.h6,
                            color = ColorBWBlack,
                        )
                        Checkbox(
                            checked = isPfChecked,
                            modifier = Modifier.padding(start = 2.dp),
                            onCheckedChange = { isPfChecked = it },
                        )
                        AppText(
                            text = stringResource(id = R.string.pf),
                            style = MaterialTheme.typography.h6,
                            color = ColorBWBlack,
                        )
                        Checkbox(
                            checked = isCChecked,
                            modifier = Modifier.padding(start = 2.dp),
                            onCheckedChange = { isCChecked = it },
                        )
                        AppText(
                            text = stringResource(id = R.string.c),
                            style = MaterialTheme.typography.h6,
                            color = ColorBWBlack,
                        )
                    }
                    AppText(
                        text = stringResource(id = R.string.teams_label),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                        fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                        modifier = Modifier
                            .padding(
                                start = dimensionResource(id = R.dimen.size_10dp),
                                end = dimensionResource(id = R.dimen.size_10dp)
                            ),
                    )
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxSize()
                            .padding(all = dimensionResource(id = R.dimen.size_10dp))
                            .background(color = Color.White)
                            .padding(top = dimensionResource(id = R.dimen.size_10dp)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Teams()
                    }

                    AppText(
                        text = stringResource(id = R.string.jersey_pref),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                        fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                        modifier = Modifier
                            .padding(
                                start = dimensionResource(id = R.dimen.size_10dp),
                                end = dimensionResource(id = R.dimen.size_10dp)
                            ),
                    )
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxSize()
                            .padding(all = dimensionResource(id = R.dimen.size_10dp))
                            .background(color = Color.White)
                            .padding(top = dimensionResource(id = R.dimen.size_10dp)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                        HorizontalLine()
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
                        HorizontalLine()
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
                        HorizontalLine()
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
                            .padding(
                                start = dimensionResource(id = R.dimen.size_10dp),
                                end = dimensionResource(id = R.dimen.size_10dp)
                            ),
                    )
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxSize()
                            .padding(all = dimensionResource(id = R.dimen.size_10dp))
                            .background(color = Color.White)
                            .padding(top = dimensionResource(id = R.dimen.size_10dp)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                        HorizontalLine()
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
                        HorizontalLine()
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
                        HorizontalLine()
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
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AppButton(
                            enabled = true,
                            icon=null,
                            themed=true,
                            onClick = { },
                            text = stringResource(id = R.string.save)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HorizontalLine() {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.appColors.material.primary)
            .fillMaxSize()
            .height(dimensionResource(id = R.dimen.size_2dp))
    )
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
            HorizontalLine()
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
            HorizontalLine()
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
            HorizontalLine()
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


