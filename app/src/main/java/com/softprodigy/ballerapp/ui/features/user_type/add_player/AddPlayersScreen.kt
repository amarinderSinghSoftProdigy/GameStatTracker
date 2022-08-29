package com.softprodigy.ballerapp.ui.features.user_type.add_player

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.validName
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.request.GlobalRequest
import com.softprodigy.ballerapp.ui.features.components.AppSearchOutlinedTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.BottomButtons
import com.softprodigy.ballerapp.ui.features.components.CoachFlowBackground
import com.softprodigy.ballerapp.ui.features.components.DeleteDialog
import com.softprodigy.ballerapp.ui.features.components.UserFlowBackground
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayBorder
import com.softprodigy.ballerapp.ui.theme.appColors
import java.util.*

@SuppressLint("MutableCollectionMutableState")
@Composable
fun AddPlayersScreen(
    vm: AddPlayerViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val search = remember { mutableStateOf(vm.teamData.search) }
    val context = LocalContext.current
    val selectedPlayer = vm.selectedPlayer
    val showDialog = remember { mutableStateOf(false) }
    val removePlayer = remember { mutableStateOf("") }
    Box(Modifier.fillMaxSize()) {
        CoachFlowBackground(
            colorCode = AppConstants.SELECTED_COLOR,
            teamLogo = UserStorage.teamLogo
        )
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_64dp)))
            AppText(
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp)),
                text = stringResource(id = R.string.add_players),
                style = MaterialTheme.typography.h3,
                color = ColorBWBlack
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            UserFlowBackground(modifier = Modifier.weight(1f)) {
                Column(
                    Modifier
                        .weight(1f)
                        .padding(all = dimensionResource(id = R.dimen.size_16dp))
                ) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        AppSearchOutlinedTextField(
                            modifier = Modifier.weight(1.0f),
                            value = search.value,
                            onValueChange = {
                                search.value = it
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_search),
                                    contentDescription = ""
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = ColorBWGrayBorder,
                                unfocusedBorderColor = ColorBWGrayBorder,
                                cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                            ),
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.search_by_name_or_email),
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                                )
                            },
                            singleLine = true
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_scanner),
                            contentDescription = "",
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.size_16dp)),
                            tint = Color.Unspecified
                        )
                    }

                    if (!validName(search.value) && search.value.isNotEmpty()) {
                        Text(
                            text = stringResource(id = R.string.valid_search),
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.size_4dp))
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                    if (search.value.isNotEmpty()) {

                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

                        PlayerListUI(modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(id = R.dimen.size_200dp)),
                            searchedText = search.value,
                            onPlayerClick = {
                                selectedPlayer.add(it)
                            })
                        Divider(modifier = Modifier
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(
                                    constraints.copy(
                                        maxWidth = constraints.maxWidth + (context.resources.getDimension(
                                            R.dimen.size_32dp
                                        )).dp.roundToPx(),
                                    )
                                )
                                layout(placeable.width, placeable.height) {
                                    placeable.place(0, 0)
                                }
                            })
                    }

                    if (selectedPlayer.isNotEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row {
                                AppText(
                                    text = stringResource(id = R.string.added_players),
                                    fontWeight = FontWeight.W500,
                                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                    color = ColorBWBlack
                                )

                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

                                AppText(
                                    text = selectedPlayer.size.toString(),
                                    fontWeight = FontWeight.W500,
                                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                    color = MaterialTheme.appColors.textField.label
                                )
                            }

                            Icon(
                                painter = painterResource(id = R.drawable.ic_info),
                                contentDescription = "",
                                modifier = Modifier.padding(dimensionResource(id = R.dimen.size_20dp)),
                                tint = Color.Unspecified
                            )
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(selectedPlayer) { filteredCountry ->
                            PlayerListItem(
                                painterResource(id = R.drawable.ic_remove),
                                countryText = filteredCountry,
                                onItemClick = { player ->
                                    removePlayer.value = player
                                    showDialog.value = true
                                },
                                showBackground = true
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_22dp)))
            BottomButtons(
                onBackClick = { onBackClick.invoke() },
                onNextClick = {
                    onNextClick.invoke()
                    val request = GlobalRequest.AddPlayers(search.value)
                    vm.saveTeamData(request)
                },
                enableState = vm.selectedPlayer.isNotEmpty()
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_22dp)))
        }
    }

    if (showDialog.value) {
        DeleteDialog(
            item = removePlayer.value,
            message = stringResource(id = R.string.alert_remove_player),
            onDismiss = { showDialog.value = false },
            onDelete = {
                selectedPlayer.remove(removePlayer.value)
                showDialog.value = false
            }
        )
    }
}

@Composable
fun PlayerListUI(
    modifier: Modifier = Modifier,
    onPlayerClick: (String) -> Unit,
    searchedText: String
) {
    val countries = getListOfPlayers()
    var filteredCountries: ArrayList<String>
    LazyColumn(modifier = modifier) {
        filteredCountries = if (searchedText.isEmpty()) {
            ArrayList()
        } else {
            val resultList = ArrayList<String>()
            for (country in countries) {
                if (country.lowercase(Locale.getDefault())
                        .contains(searchedText.lowercase(Locale.getDefault()))
                ) {
                    resultList.add(country)
                }
            }
            resultList
        }
        items(filteredCountries) { filteredCountry ->
            PlayerListItem(
                painterResource(id = R.drawable.ic_add_player),
                countryText = filteredCountry,
                onItemClick = { selectedCountry ->
                    onPlayerClick.invoke(selectedCountry)
                },
                showBackground = false
            )
        }
    }
}

@Composable
fun PlayerListItem(
    icon: Painter,
    countryText: String,
    onItemClick: (String) -> Unit,
    showBackground: Boolean
) {
    Row(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.size_48dp))
            .fillMaxWidth()
            .background(
                color = if (showBackground) {
                    MaterialTheme.appColors.material.primary
                } else {
                    Color.White
                },
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.user_demo),
                contentDescription = "",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_32dp))
                    .clip(androidx.compose.foundation.shape.CircleShape),
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            Text(
                text = countryText,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            AddRemoveButton(icon) { onItemClick(countryText) }
        }
    }

    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))

}

@Composable
fun AddRemoveButton(icon: Painter, onItemClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.size_16dp))
            .background(
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)),
                color = AppConstants.SELECTED_COLOR
            )
    ) {
        Icon(
            painter = icon, contentDescription = "",
            modifier = Modifier
                .align(Alignment.Center)
                .size(dimensionResource(id = R.dimen.size_12dp))
                .clickable(onClick = { onItemClick() }),
            tint = Color.White
        )
    }
}

fun getListOfPlayers(): ArrayList<String> {
    val isoCountryCodes = Locale.getISOCountries()
    val countryListWithEmojis = ArrayList<String>()
    for (countryCode in isoCountryCodes) {
        val locale = Locale("", countryCode)
        val countryName = locale.displayCountry
        val flagOffset = 0x1F1E6
        val asciiOffset = 0x41
        val firstChar = Character.codePointAt(countryCode, 0) - asciiOffset + flagOffset
        val secondChar = Character.codePointAt(countryCode, 1) - asciiOffset + flagOffset
        val flag =
            (String(Character.toChars(firstChar)) + String(Character.toChars(secondChar)))
        countryListWithEmojis.add("$countryName $flag")
    }
    return countryListWithEmojis
}