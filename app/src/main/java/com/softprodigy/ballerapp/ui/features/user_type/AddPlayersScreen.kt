package com.softprodigy.ballerapp.ui.features.user_type

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppSearchOutlinedTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayBorder
import java.util.*

@SuppressLint("MutableCollectionMutableState")
@Composable
fun AddPlayersScreen(onBackClick: () -> Unit, onNextClick: () -> Unit) {
    var search by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val selectedPlayer by rememberSaveable { mutableStateOf(ArrayList<String>()) }

    Box(Modifier.fillMaxSize()) {
        CoachFlowBackground()
        Column(
            Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.size_16dp))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_64dp)))
            AppText(
                text = stringResource(id = R.string.add_player),
                style = MaterialTheme.typography.h3,
                color = ColorBWBlack
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_40dp)))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.size_8dp))
                ) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        AppSearchOutlinedTextField(
                            modifier = Modifier.weight(1.0f),
                            value = search,
                            onValueChange = {
                                search = it
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_search),
                                    contentDescription = ""
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = ColorBWGrayBorder,
                                unfocusedBorderColor = ColorBWGrayBorder
                            ),
                            placeholder = { Text(text = stringResource(id = R.string.search_by_name_or_email)) }
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_scanner),
                            contentDescription = "",
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.size_16dp)),
                            tint = Color.Unspecified
                        )
                    }
                    PlayerListUI(modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.size_200dp)),
                        searchedText = search,
                        onPlayerClick = {
                            selectedPlayer.add(it)
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        })
                    Divider()
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppText(
                            text = stringResource(id = R.string.added_players),
                            fontWeight = FontWeight.W500,
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            color = ColorBWBlack
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_info),
                            contentDescription = "",
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.size_20dp)),
                            tint = Color.Unspecified
                        )
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(selectedPlayer) { filteredCountry ->
                            PlayerListItem(
                                countryText = filteredCountry,
                                onItemClick = { selectedCountry ->
//                                    onPlayerClick.invoke(selectedCountry)
                                }
                            )
                        }
                    }


                }


            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_44dp)))
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                AppButton(
                    onClick = onBackClick,
                    text = stringResource(id = R.string.back),
                    modifier = Modifier.width(dimensionResource(id = R.dimen.size_156dp)),
                    border = ButtonDefaults.outlinedBorder,
                )
                AppButton(
                    text = stringResource(id = R.string.next),
                    onClick = onNextClick,
                    icon = painterResource(id = R.drawable.ic_circle_next),
                    modifier = Modifier.width(dimensionResource(id = R.dimen.size_156dp)),
                )
            }
        }
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
                countryText = filteredCountry,
                onItemClick = { selectedCountry ->
                    onPlayerClick.invoke(selectedCountry)
                }
            )
        }
    }
}

@Composable
fun PlayerListItem(countryText: String, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = { onItemClick(countryText) })
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .padding(
                PaddingValues(
                    dimensionResource(id = R.dimen.size_12dp),
                    dimensionResource(id = R.dimen.size_8dp)
                )
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google), contentDescription = "",
            modifier = Modifier
                .size(32.dp)
                .clip(androidx.compose.foundation.shape.CircleShape),
            tint = Color.Unspecified

        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
        Text(
            text = countryText,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
        Icon(
            painter = painterResource(id = R.drawable.ic_add_player), contentDescription = "",
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.size_20dp)),
            tint = Color.Unspecified

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