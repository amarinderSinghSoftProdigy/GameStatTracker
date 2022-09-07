package com.softprodigy.ballerapp.ui.features.home.manage_team.users

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.response.ManagedUserResponse
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun ManageTeamUsersScreen(vm: ManageUserViewModel = hiltViewModel()) {

    val state = vm.manageUserUiState.value

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp))) {

            AppText(
                text = stringResource(id = R.string.coaches),
                style = MaterialTheme.typography.h4,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled

            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                items(state.coachList) { coachList ->
                    CoachList(coachList)
                }
            }
        }
    }
}

@Composable
fun CoachList(coachList: ManagedUserResponse) {

    Row(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.size_48dp))
            .fillMaxWidth()
            .background(
                Color.White,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.size_8dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.user_demo),
                contentDescription = "",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_32dp))
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            Text(
                text = coachList.name,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
            )
        }

        Row(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.size_8dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = coachList.role,
                style = MaterialTheme.typography.h6,
                color = ColorMainPrimary
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

            Image(
                painter = painterResource(id = R.drawable.ic_nav),
                contentDescription = "",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_12dp))
            )
        }
    }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))
}


sealed class ManageTeamChannel {
    data class ShowToast(val message: UiText) : ManageTeamChannel()
}