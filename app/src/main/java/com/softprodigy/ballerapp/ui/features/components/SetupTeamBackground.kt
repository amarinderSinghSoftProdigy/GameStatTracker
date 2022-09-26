package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun CoachFlowBackground(
    colorCode: String? = null,
    teamLogo: String? = "",
    click: (Options) -> Unit = {},
    content: @Composable () -> Unit
) {
    val ballColor = colorResource(id = R.color.ball_color)
    val showOptions = remember {
        mutableStateOf(false)
    }
    Surface(color = Color.Transparent) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent)
        ) {
            Surface(
                shape = CircleShape,
                color = (if (colorCode.isNullOrEmpty()) ballColor else Color(
                    android.graphics.Color.parseColor(
                        "#$colorCode"
                    )
                )).copy(alpha = 0.05F),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .absoluteOffset(
                        x = dimensionResource(id = R.dimen.size_64dp),
                        y = -dimensionResource(id = R.dimen.size_45dp)
                    )
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (colorCode.isNullOrEmpty()) ballColor else Color(
                        android.graphics.Color.parseColor(
                            "#$colorCode"
                        )
                    ),
                    modifier = Modifier
                        .padding(
                            bottom = dimensionResource(id = R.dimen.size_10dp),
                            end = dimensionResource(id = R.dimen.size_20dp),
                            start = dimensionResource(id = R.dimen.size_20dp),
                            top = dimensionResource(id = R.dimen.size_20dp)
                        )
                        .size(dimensionResource(id = R.dimen.size_200dp))
                ) {
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_200dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_ball_lines),
                            contentDescription = "center ball Icon",
                            tint = colorResource(id = R.color.black),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }



            Box(modifier = Modifier.fillMaxSize()) {
                content()
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = dimensionResource(id = R.dimen.size_16dp)),
                    horizontalAlignment = Alignment.End
                ) {
                    if (teamLogo!!.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .background(color = Color.Transparent)
                                .padding(
                                    top = dimensionResource(id = R.dimen.size_50dp),
                                    bottom = dimensionResource(id = R.dimen.size_16dp)
                                ),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            teamLogo.let {
                                CoilImage(
                                    src = it,
                                    modifier = Modifier
                                        .size(dimensionResource(id = R.dimen.size_65dp))
                                        .clip(CircleShape)
                                        .clickable {
                                            showOptions.value = !showOptions.value
                                        }
                                        .background(
                                            color = MaterialTheme.appColors.material.onSurface,
                                            CircleShape
                                        )
                                        .border(
                                            dimensionResource(id = R.dimen.size_3dp),
                                            MaterialTheme.colors.surface,
                                            CircleShape
                                        ),
                                    isCrossFadeEnabled = false,
                                    onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
                                    onError = { Placeholder(R.drawable.ic_user_profile_icon) }
                                )
                            }
                        }
                    }

                    if (showOptions.value) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.End),
                            elevation = dimensionResource(id = R.dimen.size_10dp),
                            color = Color.Transparent
                        ) {
                            Column(
                                Modifier
                                    .width(dimensionResource(id = R.dimen.size_150dp))
                                    .background(
                                        color = Color.White,
                                        RoundedCornerShape(dimensionResource(id = R.dimen.size_10dp))
                                    )
                            ) {
                                OptionItem(
                                    stringResource(id = R.string.my_profile),
                                    R.drawable.ic_profile
                                ) {
                                    click(Options.PROFILE)
                                }
                                Divider(thickness = dimensionResource(id = R.dimen.divider))
                                OptionItem(
                                    stringResource(id = R.string.swap_profiles),
                                    R.drawable.ic_swap
                                ) {
                                    click(Options.SWAP_PROFILES)
                                }
                                Divider(thickness = dimensionResource(id = R.dimen.divider))
                                OptionItem(
                                    stringResource(id = R.string.logout),
                                    R.drawable.ic_logout
                                ) {
                                    click(Options.LOGOUT)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OptionItem(label: String, resource: Int, click: () -> Unit) {

    Row(
        modifier = Modifier
            .padding(all = dimensionResource(id = R.dimen.size_16dp))
            .clickable { click() }) {
        Image(
            painter = painterResource(id = resource),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
        AppText(
            text = label,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            style = MaterialTheme.typography.h5,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
fun UserFlowBackground(
    modifier: Modifier = Modifier,
    padding: Dp? = dimensionResource(id = R.dimen.size_16dp),
    color: Color? = MaterialTheme.appColors.material.surface,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(all = padding ?: 0.dp),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
        color = color ?: MaterialTheme.appColors.material.surface
    ) {
        Column {
            content()
        }
    }
}

@Composable
fun BottomButtons(
    modifier: Modifier = Modifier,
    firstText: String = stringResource(id = R.string.back),
    secondText: String = stringResource(id = R.string.next),
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    enableState: Boolean,
    showOnlyNext: Boolean = false,
    themed: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.size_16dp),
                end = dimensionResource(id = R.dimen.size_16dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!showOnlyNext) {
            AppButton(
                text = firstText,
                icon = null,
                onClick = onBackClick,
                border = ButtonDefaults.outlinedBorder,
                modifier = Modifier.weight(1f),
                themed = themed,
            )
        } else {
            AppSpacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            )
        }
        AppSpacer(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_30dp)),
        )
        AppButton(
            text = secondText,
            onClick = onNextClick,
            icon = painterResource(id = R.drawable.ic_circle_next),
            enabled = enableState,
            modifier = Modifier.weight(1f),
            themed = themed,
        )
    }
}

enum class Options {
    PROFILE,
    SWAP_PROFILES,
    LOGOUT,
    //Add options on the Profile dropwodn.
}