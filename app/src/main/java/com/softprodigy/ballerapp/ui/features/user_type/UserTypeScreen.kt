package com.softprodigy.ballerapp.ui.features.user_type

import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.theme.spacing

@Composable
fun UserTypeScreen(onNextClick: (String) -> Unit) {
    Box(
        Modifier.fillMaxWidth()
    ) {
        CoachFlowBackground(
            modifier = Modifier.fillMaxSize(),
            outerIcon = painterResource(id = R.drawable.ic_ball),
//            innerIcon = painterResource(id = R.drawable.ic_ball_green),
//            centerIcon = painterResource(id = R.drawable.ic_google),
        )
        UserTypeSelector(onNextClick = onNextClick)
    }
}


@Composable
fun UserTypeSelector(onNextClick: (String) -> Unit) {
    val options = listOf(
        AppConstants.USER_TYPE_PLAYER,
        AppConstants.USER_TYPE_PARENT,
        AppConstants.USER_TYPE_COACH,
    )
    var selectedUserType by remember {
        mutableStateOf("")
    }
    val onSelectionChange = { text: String ->
        selectedUserType = text
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        options.forEach { text ->
            Row(
                modifier = Modifier
                    .padding(
                        all = 8.dp,
                    ),
            ) {
                AppButton(
                    onClick = {
                        onSelectionChange(text)
                    }, colors = if (text == selectedUserType) {
                        ButtonDefaults.buttonColors()
                    } else {
                        ButtonDefaults.outlinedButtonColors()
                    },
                    border = if (text == selectedUserType) {
                        null
                    } else {
                        ButtonDefaults.outlinedBorder
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(text = text)
                }
            }
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        Row(
            Modifier
                .align(Alignment.End)
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
        ) {
            AppButton(onClick = {
                onNextClick.invoke(selectedUserType)
            },
            enabled = selectedUserType.isNotEmpty()) {
                Text(text = stringResource(id = R.string.next))
            }
        }

    }
}
