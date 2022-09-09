package com.softprodigy.ballerapp.ui.features.profile


import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.common.api.ApiException
import com.softprodigy.ballerapp.LocalFacebookCallbackManager
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.CustomFBManager
import com.softprodigy.ballerapp.common.FacebookUserProfile
import com.softprodigy.ballerapp.common.GoogleApiContract
import com.softprodigy.ballerapp.common.RequestCode
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.common.isValidPassword
import com.softprodigy.ballerapp.common.passwordMatches
import com.softprodigy.ballerapp.data.SocialUserModel
import com.softprodigy.ballerapp.data.request.SignUpData
import com.softprodigy.ballerapp.data.response.Team
import com.softprodigy.ballerapp.data.response.UserInfo
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.theme.*
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileTab(
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_20dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_40dp)))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(color = Color.White)
                    .padding(all = dimensionResource(id = R.dimen.size_16dp)),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                ) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                    Image(
                        painter = painterResource(id = R.drawable.user_demo),
                        contentDescription = "",
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_200dp))
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))

                    AppText(
                        text = "George Will",
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                        fontSize = dimensionResource(id = R.dimen.txt_size_20).value.sp
                    )
                    Row(
                        modifier = Modifier

                    ) {
                        DetailItem("email", "joe@gmail.com")
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                        DetailItem("number", "9888834352")
                    }
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(color = Color.White)
                    .padding(all = dimensionResource(id = R.dimen.size_16dp)),
            ) {
                Column() {
                    AppText(
                        text = stringResource(id = R.string.parents),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ParentItem(stringResource(id = R.string.mother), "Alena Culhane", "")
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_5dp)))
                        ParentItem(stringResource(id = R.string.father), "Brandon Culhan", "")
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            ProfileItem(stringResource(id = R.string.mother), "May 15, 1999")
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            ProfileItem(stringResource(id = R.string.classof), "2025")
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            ProfileItem(stringResource(id = R.string.positons), "PG,SG")
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            TeamList()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .background(color = Color.White)
                    .padding(all = dimensionResource(id = R.dimen.size_16dp)),
            ) {
                Column() {
                    AppText(
                        text = stringResource(id = R.string.jersey_pref),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                        fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                    PreferenceItem(
                        stringResource(id = R.string.jersey_number),
                        "23, 16, 18",
                        stringResource(id = R.string.gender),
                        "Male"
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                    PreferenceItem(
                        stringResource(id = R.string.shirt_size),
                        "Adult, L",
                        stringResource(id = R.string.waist_size),
                        "32"
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .background(color = Color.White)
                    .padding(all = dimensionResource(id = R.dimen.size_16dp)),
            ) {
                Column() {
                    AppText(
                        text = stringResource(id = R.string.fun_facts),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                        fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                    PreferenceItem(
                        stringResource(id = R.string.favorite_college_team),
                        "Favorite College Team",
                        stringResource(id = R.string.favorite_nba_team),
                        "Team Name"
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                    PreferenceItem(
                        stringResource(id = R.string.favorite_active_player),
                        "Player Name",
                        stringResource(id = R.string.favoritea_all_time_tlayer),
                        "Player Name"
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_80dp)))
        }
    }
}

@Composable
fun ProfileItem(type: String, value: String) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(color = Color.White)
            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AppText(
                text = type,
                style = MaterialTheme.typography.h6,
                color = ColorBWBlack
            )

            AppText(
                text = value,
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
            )
        }
    }
}

@Composable
fun RowScope.ParentItem(relation: String, value: String, imageUrl: String) {

    Row(
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_demo),
            contentDescription = "",
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.size_40dp))
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_10dp)),
        ) {
            AppText(
                text = relation,
                style = MaterialTheme.typography.h6,
                color = ColorBWBlack
            )
            AppText(
                text = value,
                style = MaterialTheme.typography.h6,
                color = text_field_label
            )
        }

    }
}

@Composable
fun RowScope.DetailItem(stringId: String, value: String) {
    Column(
        modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            text = stringResourceByName(stringId),
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
        )
        AppText(
            text = value,
            style = MaterialTheme.typography.h6,
            color = text_field_label
        )
    }
}

@Composable
fun TeamList() {
    var teams: Array<Team> = arrayOf(
        Team(name = "Springfield Bucks", status = "Player"),
        Team(name = "Springfield Sprouts", status = "Program Manger")
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(color = Color.White)
            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {
            AppText(
                text = stringResource(id = R.string.teams_label),
                style = MaterialTheme.typography.h6,
                color = ColorBWBlack
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            teams.forEach { team ->
                Column(
                ) {
                    Row() {
                        Image(
                            painter = painterResource(id = R.drawable.user_demo),
                            contentDescription = "",
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_40dp))
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
                        Column(
                            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_10dp)),
                        ) {
                            AppText(
                                text = team.name,
                                style = MaterialTheme.typography.h6,
                                color = ColorBWBlack
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
                            AppText(
                                text = team.status,
                                style = MaterialTheme.typography.h6,
                                color = ColorMainPrimary
                            )

                        }
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
                }
            }
        }
//
    }
}

@Composable
private fun PreferenceItem(
    firstKey: String,
    firstValue: String,
    secondKey: String,
    secondValue: String
) {
    Column(
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                AppText(
                    text = firstKey,
                    style = MaterialTheme.typography.h6,
                    color = text_field_label,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
                AppText(
                    text = firstValue,
                    style = MaterialTheme.typography.h6,
                    color = ColorBWBlack
                )

            }
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_5dp)))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                AppText(
                    text = secondKey,
                    style = MaterialTheme.typography.h6,
                    color = text_field_label
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
                AppText(
                    text = secondValue,
                    style = MaterialTheme.typography.h6,
                    color = ColorBWBlack
                )

            }

        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
    }
}

