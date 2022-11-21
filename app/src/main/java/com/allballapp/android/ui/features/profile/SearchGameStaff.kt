package com.allballapp.android.ui.features.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.allballapp.android.data.response.GetSearchStaff
import com.allballapp.android.ui.features.components.AppOutlineTextField
import com.allballapp.android.ui.features.components.AppText
import com.allballapp.android.ui.features.components.CoilImage
import com.allballapp.android.ui.features.components.Placeholder
import com.allballapp.android.ui.theme.ColorBWBlack
import com.allballapp.android.ui.theme.appColors
import com.allballapp.android.ui.theme.rubikFamily
import com.allballapp.android.R
@Composable
fun SearchGameStaff(
    vm: ProfileViewModel,
    onGameStaffClick: () -> Unit
) {

    val state = vm.state.value

    Column(
        Modifier
            .fillMaxSize()
            .padding(
                vertical = dimensionResource(id = R.dimen.size_10dp),
                horizontal = dimensionResource(id = R.dimen.size_16dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppOutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.searchGameStaff,
            onValueChange = {
                if (it.length <= 30)
                    vm.onEvent(ProfileEvent.OnGameStaffChanges(it))
            },
            placeholder = {
                AppText(
                    text = stringResource(id = R.string.search_venue_by_name),
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                focusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                cursorColor = MaterialTheme.appColors.buttonColor.backgroundEnabled,
                textColor = MaterialTheme.appColors.buttonColor.backgroundEnabled,
            ),
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                if (state.isLoading)
                    CircularProgressIndicator(color = ColorBWBlack)
            }
            items(state.searchGameStaffList) { searchGameStaff ->
                GameSearchList(searchStaff = searchGameStaff, onSearchStaffClick = {
                    vm.onEvent(ProfileEvent.OnSelectedGameStaff(searchGameStaff))
                    onGameStaffClick.invoke()
                })
            }
        }
    }
}

@Composable
fun GameSearchList(searchStaff: GetSearchStaff, onSearchStaffClick: () -> Unit) {
    Column {
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onSearchStaffClick.invoke() }
            .padding(dimensionResource(id = R.dimen.size_8dp)),
            verticalAlignment = Alignment.CenterVertically)
        {
            CoilImage(
                src = com.allballapp.android.BuildConfig.IMAGE_SERVER + searchStaff.profileImage,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_40dp))
                    .clip(CircleShape),
                isCrossFadeEnabled = false,
                onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
                onError = { Placeholder(R.drawable.ic_user_profile_icon) }
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))

            AppText(
                text = if (!searchStaff.name.isNullOrEmpty()) searchStaff.name else "",
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = rubikFamily,
                color = MaterialTheme.appColors.buttonColor.backgroundEnabled
            )

        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
    }
}

