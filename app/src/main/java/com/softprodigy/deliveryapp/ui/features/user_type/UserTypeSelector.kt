package com.softprodigy.deliveryapp.ui.features.user_type

import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.softprodigy.deliveryapp.R
import com.softprodigy.deliveryapp.ui.features.components.AppButton
import com.softprodigy.deliveryapp.ui.theme.spacing

@Composable
fun UserTypeScreen() {
    Box(
        Modifier.fillMaxWidth()
    ) {
        CoachFlowBackground(
            modifier = Modifier.fillMaxSize(),
            outerIcon = painterResource(id = R.drawable.ic_ball),
//            innerIcon = painterResource(id = R.drawable.ic_ball_green),
//            centerIcon = painterResource(id = R.drawable.ic_google),
        )
        UserTypeSelector()
    }
}


@Composable
fun UserTypeSelector() {
    val options = listOf(
        "Player",
        "Parent",
        "Coach",
    )
    var selectedOption by remember {
        mutableStateOf("")
    }
    val onSelectionChange = { text: String ->
        selectedOption = text
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
                    }, colors = if (text == selectedOption) {
                        ButtonDefaults.buttonColors()
                    } else {
                        ButtonDefaults.outlinedButtonColors()
                    },
                    border = if (text == selectedOption) {
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

        Row(Modifier.align(Alignment.End)) {
            AppButton(onClick = { }) {
                Text(text = stringResource(id = R.string.next))
            }
        }

    }
}
