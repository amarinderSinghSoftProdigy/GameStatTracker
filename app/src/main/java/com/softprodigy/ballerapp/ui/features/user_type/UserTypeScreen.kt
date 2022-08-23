package com.softprodigy.ballerapp.ui.features.user_type

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.BottomButtons
import com.softprodigy.ballerapp.ui.features.components.CoachFlowBackground
import com.softprodigy.ballerapp.ui.features.components.UserSelectionSurface
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.spacing

@Composable
fun UserTypeScreen(onNextClick: (String) -> Unit) {
    Box(
        Modifier.fillMaxWidth()
    ) {
        CoachFlowBackground()
        UserTypeSelector(onNextClick = onNextClick)
    }
}


@Composable
fun UserTypeSelector(onNextClick: (String) -> Unit) {
    val options = listOf(
        AppConstants.USER_TYPE_PLAYER,
        AppConstants.USER_TYPE_COACH,
        AppConstants.USER_TYPE_REFEREE
    )
    var selectedUserType by remember {
        mutableStateOf("")
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

            AppText(
                text = stringResource(id = R.string.what_type_of_user_are_you),
                style = MaterialTheme.typography.h3,
                color = ColorBWBlack
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            options.forEach { text ->
                Row(
                    modifier = Modifier
                        .padding(
                            all = dimensionResource(id = R.dimen.size_8dp),
                        ),
                ) {
                    UserSelectionSurface(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        text = text,
                        isSelected = text == selectedUserType,
                        onClick = { onSelectionChange(text) }
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
                onNextClick = { onNextClick.invoke(selectedUserType) },
                enableState = selectedUserType.isNotEmpty(),
                showOnlyNext = true,
            )
        }


    }
}
