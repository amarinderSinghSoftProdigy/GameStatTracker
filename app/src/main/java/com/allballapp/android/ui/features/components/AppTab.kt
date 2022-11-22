package com.allballapp.android.ui.features.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.allballapp.android.R
import com.allballapp.android.ui.theme.appColors
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset

@Composable
fun AppTab(modifier: Modifier = Modifier, title: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .background(
                color = if (selected) MaterialTheme.appColors.material.primaryVariant else Color.Transparent,
                shape = RoundedCornerShape(
                    dimensionResource(id = R.dimen.size_18dp)
                )
            )
            .clickable { onClick.invoke() }
    ) {
        AppText(
            text = title,
            fontWeight = FontWeight.W600,
            fontSize = dimensionResource(R.dimen.txt_size_12).value.sp,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(
                    vertical = dimensionResource(id = R.dimen.size_10dp),
                    horizontal = dimensionResource(
                        id = R.dimen.size_12dp
                    )
                ),
            color = if (selected) MaterialTheme.appColors.buttonColor.textEnabled else MaterialTheme.appColors.buttonColor.textDisabled,
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppScrollableTabRow(
    pagerState: PagerState, tabs: @Composable @UiComposable () -> Unit
) {
    ScrollableTabRow(
        divider = {},
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                color = MaterialTheme.appColors.material.primaryVariant
            )
        },
        backgroundColor = MaterialTheme.appColors.material.surface,
        edgePadding = dimensionResource(id = R.dimen.size_25dp), tabs = tabs,

        )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppStaticTabRow(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    tabs: @Composable @UiComposable () -> Unit
) {
    TabRow(
        divider = {},
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                color = MaterialTheme.appColors.material.primaryVariant
            )
        },
        backgroundColor = MaterialTheme.appColors.material.surface,
        tabs = tabs,

        )
}

@Composable
fun AppTabLikeViewPager(
    modifier: Modifier = Modifier,
    title: String,
    painter: Painter,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Tab(modifier = modifier, selected = selected, onClick = onClick, text = {
        Row(
            verticalAlignment = Alignment.CenterVertically,

        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                tint = if (selected) MaterialTheme.appColors.material.primaryVariant else MaterialTheme.appColors.textField.label
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
            Text(
                stringResourceByName(name = title),
                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                color = if (selected) MaterialTheme.appColors.textField.labelColor else MaterialTheme.appColors.textField.label
            )
        }

    })
}

@Composable
fun AppSingleTabLikeViewPager(
    modifier: Modifier = Modifier,
    title: String,
    painter: Painter,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Tab(modifier = modifier, selected = selected, onClick = onClick, text = {

        Column(verticalArrangement = Arrangement.Center) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_14dp)))
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painter,
                    contentDescription = null,
                    tint = if (selected) MaterialTheme.appColors.material.primaryVariant else MaterialTheme.appColors.textField.label
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                Text(
                    stringResourceByName(name = title),
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    color = if (selected) MaterialTheme.appColors.textField.labelColor else MaterialTheme.appColors.textField.label
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_14dp)))
            Divider(
                color = MaterialTheme.appColors.material.primaryVariant, modifier = Modifier
                    .width(
                        dimensionResource(id = R.dimen.size_70dp)
                    )
                    .height(dimensionResource(id = R.dimen.size_2dp))
            )
        }

    })
}