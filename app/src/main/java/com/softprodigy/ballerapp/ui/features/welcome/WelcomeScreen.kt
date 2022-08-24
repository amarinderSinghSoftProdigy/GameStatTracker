package com.softprodigy.ballerapp.ui.features.welcome

import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.theme.spacing
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.BottomButtons
import kotlinx.coroutines.launch

data class WelcomeScreenData(val image: Int, val title: String, val description: String)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreen(onNextScreen: () -> Unit) {

    val scope = rememberCoroutineScope()
    val items = ArrayList<WelcomeScreenData>()
    items.add(
        WelcomeScreenData(
            R.drawable.ball, stringResource(id = R.string.welcome_to_the_app), stringResource(
                id = R.string.desc
            )
        )
    )
    items.add(
        WelcomeScreenData(
            R.drawable.ball, stringResource(id = R.string.welcome_to_the_app), stringResource(
                id = R.string.desc
            )
        )
    )
    items.add(
        WelcomeScreenData(
            R.drawable.ball, stringResource(id = R.string.welcome_to_the_app), stringResource(
                id = R.string.desc
            )
        )
    )
    val pagerState = rememberPagerState(
        pageCount = items.size,
        initialOffScreenLimit = 2,
        infiniteLoop = false,
        initialPage = 0
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = dimensionResource(id = R.dimen.size_16dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) { page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.size_12dp)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = items[page].image),
                        contentDescription = items[page].title,
                        modifier = Modifier
                            .width(dimensionResource(id = R.dimen.size_200dp))
                            .height(
                                dimensionResource(id = R.dimen.size_200dp)
                            ),
                        contentScale = ContentScale.FillBounds
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

                    AppText(
                        text = items[page].title,
                        style = MaterialTheme.typography.h1,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                    AppText(
                        text = items[page].description,
                        style = MaterialTheme.typography.body1,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
            PagerIndicator(size = items.size, currentPage = pagerState.currentPage)
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 5.dp)
        ) {
            BottomSection(pagerState.currentPage, onNextScreen) {
                scope.launch {
                    pagerState.scrollToPage(pagerState.currentPage + 1)
                }
            }
        }

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun rememberPagerState(
    @androidx.annotation.IntRange(from = 0) pageCount: Int,
    @androidx.annotation.IntRange(from = 0) initialPage: Int = 0,
    @FloatRange(from = 0.0, to = 1.0) initialPageOffset: Float = 0f,
    @androidx.annotation.IntRange(from = 1) initialOffScreenLimit: Int = 1,
    infiniteLoop: Boolean = false
): PagerState = rememberSaveable(saver = PagerState.Saver) {

    PagerState(
        pageCount = pageCount,
        currentPage = initialPage,
        currentPageOffset = initialPageOffset,
        offscreenLimit = initialOffScreenLimit,
        infiniteLoop = infiniteLoop
    )

}

@Composable
fun PagerIndicator(size: Int, currentPage: Int) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        repeat(size) {
            Indicator(isSelected = it == currentPage)
        }
    }

}

@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(targetValue = if (isSelected) 10.dp else 10.dp)

    Box(
        modifier = Modifier
            .padding(1.dp)
            .height(10.dp)
            .width(width = width.value)
            .clip(CircleShape)
            .background(if (isSelected) Color.Black else Color.Gray)
    )
}

@Composable
fun BottomSection(currentPager: Int, onNextScreen: () -> Unit, onNextPage: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        if (currentPager == 2) {
            AppButton(
                singleButton = true,
                enabled = true,
                onClick = {
                    onNextScreen()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(
                        start = dimensionResource(id = R.dimen.size_16dp),
                        end = dimensionResource(id = R.dimen.size_16dp),
                    ),
                text = stringResource(id = R.string.get_started),
                icon = painterResource(id = R.drawable.ic_circle_next)
            )
        } else {
            BottomButtons(
                onBackClick = { onNextScreen() },
                onNextClick = {
                    onNextPage()
                },
                enableState = true,
                firstText = stringResource(id = R.string.skip),
                secondText = stringResource(id = R.string.next)
            )
        }
    }
}

