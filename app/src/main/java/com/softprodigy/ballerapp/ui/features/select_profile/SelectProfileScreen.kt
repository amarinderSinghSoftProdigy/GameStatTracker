package com.softprodigy.ballerapp.ui.features.select_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.SelectProfileResponse
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.BottomButtons
import com.softprodigy.ballerapp.ui.features.components.CoachFlowBackground
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun SelectProfileScreen(vm: SelectProfileViewModel = hiltViewModel(), onNextClick: () -> Unit) {

    val state = vm.selectProfileUiState.value

    CoachFlowBackground() {
        Box(
            Modifier
                .fillMaxSize()
        ) {


            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {

                AppText(
                    text = stringResource(id = R.string.select_profile),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))

                Column(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                ) {
                    state.selectProfileList.forEachIndexed { index, it ->

                        SelectProfileItems(
                            it = it,
                            isSelected = it.role == state.isSelectedRole,
                            index = index,
                            selectProfileList = state.selectProfileList
                        ) {

                            vm.onEvent(SelectProfileUIEvent.IsSelectedRole(it.role))
                        }
                    }
                }
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = dimensionResource(id = R.dimen.size_30dp))
            )
            {
                BottomButtons(
                    firstText = stringResource(id = R.string.back),
                    secondText = stringResource(id = R.string.next),
                    onBackClick = { },
                    onNextClick = {
                        onNextClick()
                    },
                    enableState = state.isSelectedRole.isNotEmpty(),
                    showOnlyNext = true,
                )
            }
        }
    }
}

@Composable
fun SelectProfileItems(
    it: SelectProfileResponse,
    isSelected: Boolean,
    index: Int,
    selectProfileList: List<SelectProfileResponse>,
    SelectedUser: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) MaterialTheme.appColors.buttonColor.bckgroundEnabled else Color.White,
                shape = (when {
                    index == 0 -> {
                        RoundedCornerShape(
                            topStart = dimensionResource(
                                id = R.dimen.size_8dp
                            ),
                            topEnd = dimensionResource(
                                id = R.dimen.size_8dp
                            )
                        )
                    }
                    selectProfileList.size == index + 1 -> {
                        RoundedCornerShape(
                            bottomEnd = dimensionResource(
                                id = R.dimen.size_8dp
                            ),
                            bottomStart = dimensionResource(
                                id = R.dimen.size_8dp
                            )
                        )
                    }
                    else -> {
                        RectangleShape
                    }
                })


            )
            .clickable {
                SelectedUser()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Row(
            modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ball),
                contentDescription = null,
                modifier = Modifier
                    .size(
                        dimensionResource(id = R.dimen.size_64dp)
                    )
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))

            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                AppText(
                    text = it.name,
                    style = MaterialTheme.typography.h3,
                    color = if (!isSelected) MaterialTheme.appColors.buttonColor.bckgroundEnabled else Color.White
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                AppText(
                    text = it.role,
                    style = MaterialTheme.typography.h6,
                    color = ColorMainPrimary,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
    Divider(color = MaterialTheme.appColors.material.primary)
}