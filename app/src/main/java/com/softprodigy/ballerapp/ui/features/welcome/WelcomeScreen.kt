package com.softprodigy.ballerapp.ui.features.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.BottomButtons
import com.softprodigy.ballerapp.ui.features.components.PagerIndicator
import com.softprodigy.ballerapp.ui.features.components.rememberPagerState
import com.softprodigy.ballerapp.ui.theme.spacing
import kotlinx.coroutines.launch

data class WelcomeScreenData(val image: Int, val title: String, val description: String)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreen(onNextScreen: () -> Unit) {

    val scope = rememberCoroutineScope()
    val items = ArrayList<WelcomeScreenData>()
    items.add(
        WelcomeScreenData(
            R.drawable.ic_logo, stringResource(id = R.string.welcome_to_the_app), stringResource(
                id = R.string.desc
            )
        )
    )
    items.add(
        WelcomeScreenData(
            R.drawable.ic_logo, stringResource(id = R.string.welcome_to_the_app), stringResource(
                id = R.string.desc
            )
        )
    )
    items.add(
        WelcomeScreenData(
            R.drawable.ic_logo, stringResource(id = R.string.welcome_to_the_app), stringResource(
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
                    .fillMaxHeight(0.74f)
            ) { page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.size_12dp)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.size_95dp))
                            .width(dimensionResource(id = R.dimen.size_160dp)),
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
                .padding(bottom = dimensionResource(id = R.dimen.size_5dp))
        ) {
            BottomSection(pagerState.currentPage, onNextScreen) {
                scope.launch {
                    pagerState.scrollToPage(pagerState.currentPage + 1)
                }
            }
        }
    }
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
                    .height(dimensionResource(id = R.dimen.size_50dp))
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
                secondText = stringResource(id = R.string.next),
            )
        }
    }
}

