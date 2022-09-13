package com.softprodigy.ballerapp.ui.features.user_type

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.BottomButtons
import com.softprodigy.ballerapp.ui.features.components.CoachFlowBackground
import com.softprodigy.ballerapp.ui.features.components.UserSelectionSurface
import com.softprodigy.ballerapp.ui.features.components.UserType
import com.softprodigy.ballerapp.ui.features.components.stringResourceByName
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpUIEvent
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpViewModel
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack


@Composable
fun UserTypeScreen(
    onNextClick: (String) -> Unit,
    signUpvm: SignUpViewModel
) {
    CoachFlowBackground()
    {
        Box(
            Modifier.fillMaxWidth()
        ) {
            UserTypeSelector(onNextClick = onNextClick, signUpvm/*, signUpData*/)
        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun UserTypeSelector(
    onNextClick: (String) -> Unit,
    signUpvm: SignUpViewModel,
) {

    val options = listOf(
        UserType.PLAYER,
        UserType.COACH,
        UserType.PARENT,
        UserType.GAME_STAFF,
        UserType.PROGRAM_STAFF,
        UserType.FAN,
    )
    val state = signUpvm.signUpUiState.value

    val selectedUserType = remember {
        mutableStateOf(state.signUpData.role)
    }

    val onSelectionChange = { text: String ->
        selectedUserType.value = text
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val column = createRef()
        val button = createRef()


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.constrainAs(column) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
            AppText(
                text = stringResource(id = R.string.what_type_of_user_are_you),
                style = MaterialTheme.typography.h3,
                color = ColorBWBlack
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_30dp)))

            options.forEach { user ->
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.size_8dp),
                            vertical = dimensionResource(id = R.dimen.size_4dp)
                        ),
                ) {
                    val name = stringResourceByName(name = user.stringId)
                    UserSelectionSurface(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        text = name,
                        isSelected = user.key == selectedUserType.value,
                        onClick = {
                            onSelectionChange(user.key)
                            signUpvm.onEvent(
                                SignUpUIEvent.OnRoleChanged(
                                    user.key
                                )
                            )
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_30dp)))
        Box(
            Modifier
                .fillMaxWidth()
                .constrainAs(button) {
                    top.linkTo(column.bottom)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })
        {
            BottomButtons(
                firstText = stringResource(id = R.string.back),
                secondText = stringResource(id = R.string.next),
                onBackClick = { },
                onNextClick = {
                    onNextClick.invoke(selectedUserType.value ?: "")
                },
                enableState = (selectedUserType.value ?: "").isNotEmpty(),
                showOnlyNext = true,
            )
        }
    }
}
