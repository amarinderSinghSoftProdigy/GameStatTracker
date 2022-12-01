package com.allballapp.android.ui.features.home.home_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.allballapp.android.BuildConfig
import com.allballapp.android.R
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.HomeViewModel
import com.allballapp.android.ui.theme.ColorBWBlack

@Composable
fun SettingsScreen(
    type: String = BuildConfig.API_SERVER,
    vm: HomeViewModel,
    onNextClick: (String) -> Unit
) {
    val showDialog = remember {
        mutableStateOf(false)
    }

    val env = remember {
        mutableStateOf(type)
    }
    val state = vm.state.value
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxSize()
        ) {
            LazyColumn(Modifier.fillMaxWidth()) {
                items(state.evnList) { item ->
                    EnvItem(item, env.value) {
                        if (it != type) {
                            env.value = it
                            showDialog.value = true
                        } else {
                            showDialog.value = false
                        }
                    }
                }
            }
        }
    }

    if (showDialog.value) {
        ConfirmDialog(
            title = stringResource(id = R.string.env_change_message),
            onDismiss = {
                env.value = type
                showDialog.value = false
            },
            onConfirmClick = {
                vm.onEvent(HomeScreenEvent.OnEnvUpdate(env.value))
                onNextClick(env.value)
                showDialog.value = false
            })
    }

}

@Composable
fun EnvItem(item: Environment, selected: String, OnNextClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                OnNextClick(item.value)
            }
    ) {
        Row(modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp))) {
            Text(
                text = stringResourceByName(name = item.key),
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                fontWeight = FontWeight.W500,
                color = ColorBWBlack,
                modifier = Modifier.weight(1F)
            )
            CustomCheckBox(item.value == selected) {}
        }
        DividerCommon()
    }
}
