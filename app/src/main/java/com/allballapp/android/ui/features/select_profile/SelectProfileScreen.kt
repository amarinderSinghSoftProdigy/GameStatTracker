package com.allballapp.android.ui.features.select_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.allballapp.android.R
import com.allballapp.android.data.response.SwapUser
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.sign_up.SignUpChannel
import com.allballapp.android.ui.features.sign_up.SignUpUIEvent
import com.allballapp.android.ui.features.sign_up.SignUpViewModel
import com.allballapp.android.ui.theme.ColorMainPrimary
import com.allballapp.android.ui.theme.appColors

@Composable
fun SelectProfileScreen(vm: SignUpViewModel, onNextClick: () -> Unit) {

    val state = vm.signUpUiState.value
    val context = LocalContext.current
    val id = remember {
        mutableStateOf(SwapUser())
    }


    /*remember {
       vm.onEvent(HomeScreenEvent.OnSwapClick)
    }*/
    LaunchedEffect(key1 = Unit) {
        vm.signUpChannel.collect { uiEvent ->
            when (uiEvent) {
                is SignUpChannel.OnProfileUpdateSuccess -> {
                   /* Toast.makeText(
                        context,
                        uiEvent.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()*/
                    onNextClick()
                }
            }
        }
    }
    CoachFlowBackground {}
    Box(
        Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = dimensionResource(id = R.dimen.size_100dp))
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_64dp)))
            AppText(
                text = stringResource(id = R.string.select_profile),
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))

            Column(
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.size_16dp),
                        vertical = dimensionResource(id = R.dimen.size_16dp)
                    ),
                verticalArrangement = Arrangement.Center
            ) {
                 LazyColumn {
                    itemsIndexed(state.profileList) { index, item ->
                        SelectProfileItems(
                            size = state.profileList.size,
                            index = index,
                            users = item,
                            isSelected = id.value._Id == item._Id,
                            onConfirmClick = {
                                id.value = item
                            },
                        )
                    }
                }
            }
        }

        Box(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = dimensionResource(id = R.dimen.size_50dp))
        )
        {
            BottomButtons(
                firstText = stringResource(id = R.string.back),
                secondText = stringResource(id = R.string.next),
                onBackClick = { },
                onNextClick = {
                    vm.onEvent(SignUpUIEvent.OnSwapUpdate(id.value))
                },
                enableState = id.value._Id.isNotEmpty(),
                showOnlyNext = true,
            )
        }
    }
    if (state.isLoading) {
        CommonProgressBar()
    }
}

@Composable
fun SelectProfileItems(
    size: Int,
    index: Int,
    users: SwapUser,
    isSelected: Boolean,
    onConfirmClick: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) MaterialTheme.appColors.buttonColor.bckgroundEnabled else Color.White,
                shape = (when {
                    size == 1 -> {
                        RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                    }
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
                    size == index + 1 -> {
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
                onConfirmClick()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Row(
            modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoilImage(
                src = com.allballapp.android.BuildConfig.IMAGE_SERVER + users.profileImage,
                modifier = Modifier
                    .size(
                        dimensionResource(id = R.dimen.size_64dp)
                    )
                    .clip(CircleShape)
                    .border(
                        dimensionResource(id = R.dimen.size_2dp),
                        MaterialTheme.colors.surface,
                        CircleShape,
                    ),
                isCrossFadeEnabled = false,
                onLoading = { Placeholder(R.drawable.ic_profile_placeholder) },
                onError = { Placeholder(R.drawable.ic_profile_placeholder) }
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))

            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                AppText(
                    text = users.firstName + " " + users.lastName,
                    style = MaterialTheme.typography.h3,
                    color = if (!isSelected) MaterialTheme.appColors.buttonColor.bckgroundEnabled else Color.White
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
//                val name = CommonUtils.getRole(users.role)
                AppText(
//                    text = stringResourceByName(name = name.ifEmpty { "user_type" }),
                    text = users.phone,
                    style = MaterialTheme.typography.h6,
                    color = ColorMainPrimary,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
    AppDivider(color = MaterialTheme.appColors.material.primary)
}