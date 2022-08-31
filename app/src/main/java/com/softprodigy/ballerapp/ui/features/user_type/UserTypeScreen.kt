package com.softprodigy.ballerapp.ui.features.user_type

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.request.GlobalRequest
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.BottomButtons
import com.softprodigy.ballerapp.ui.features.components.CoachFlowBackground
import com.softprodigy.ballerapp.ui.features.components.UserSelectionSurface
import com.softprodigy.ballerapp.ui.features.components.UserType
import com.softprodigy.ballerapp.ui.features.components.stringResourceByName
import com.softprodigy.ballerapp.data.request.SignUpData
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack


@Composable
fun UserTypeScreen(
    signUpData: SignUpData?,
    onNextClick: (String, SignUpData) -> Unit,
    viewModel: UserTypeViewModel = hiltViewModel()
) {
    Box(
        Modifier.fillMaxWidth()
    ) {
        CoachFlowBackground()
        UserTypeSelector(onNextClick = onNextClick, viewModel, signUpData)
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun UserTypeSelector(
    onNextClick: (String, SignUpData) -> Unit,
    viewModel: UserTypeViewModel,
    signUpData: SignUpData?
) {
    val options = listOf(
        UserType.PLAYER,
        UserType.COACH,
        UserType.PARENT,
        UserType.GAME_STAFF,
        UserType.PROGRAM_STAFF,
        UserType.FAN,
    )

    var selectedUserType by remember {
        mutableStateOf(viewModel.userRole)
    }

    val onSelectionChange = { text: String ->
        selectedUserType = text
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

            options.forEach { it ->
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.size_8dp),
                            vertical = dimensionResource(id = R.dimen.size_4dp)
                        ),
                ) {
                    val name = stringResourceByName(name = it.stringId)
                    UserSelectionSurface(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        text = name,
                        isSelected = name == selectedUserType,
                        onClick = { onSelectionChange(name) }
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
                }) {
            BottomButtons(
                firstText = stringResource(id = R.string.back),
                secondText = stringResource(id = R.string.next),
                onBackClick = { },
                onNextClick = {
                    val signUpData = SignUpData(
                        email = signUpData?.email,
                        address = signUpData?.address,
                        birthdate = signUpData?.birthdate,
                        gender = signUpData?.gender,
                        password = signUpData?.password,
                        repeatPassword = signUpData?.repeatPassword,
                        role = selectedUserType
                    )
                    onNextClick.invoke(selectedUserType, signUpData)
                    val userRole = GlobalRequest.Users(selectedUserType)
                    viewModel.saveData(userRole)
                },
                enableState = selectedUserType.isNotEmpty(),
                showOnlyNext = true,
            )
        }
    }
}
