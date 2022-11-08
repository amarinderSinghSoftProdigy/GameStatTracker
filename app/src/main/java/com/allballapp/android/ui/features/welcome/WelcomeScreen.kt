package com.allballapp.android.ui.features.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.allballapp.android.ui.features.components.AppText
import com.allballapp.android.ui.features.components.PagerIndicator
import com.allballapp.android.ui.features.components.SingleWalkthroughButtonView
import com.allballapp.android.ui.features.components.rememberPagerState
import com.allballapp.android.ui.theme.ColorGreyLighter
import com.allballapp.android.ui.theme.SkipColor
import kotlinx.coroutines.launch
import com.allballapp.android.R

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreen(onNextScreen: () -> Unit) {

    data class Walkthrough(val painter: Painter, val text: String)

    val scope = rememberCoroutineScope()
    val items = arrayListOf(
        Walkthrough(
            painterResource(id = R.drawable.onboarding_1),
            stringResource(id = R.string.skip)
        ),
        Walkthrough(
            painterResource(id = R.drawable.onboarding_2),
            stringResource(id = R.string.skip)
        ),
        Walkthrough(
            painterResource(id = R.drawable.onboarding_3),
            stringResource(id = R.string.skip)
        ),
        Walkthrough(
            painterResource(id = R.drawable.onboarding_4),
            stringResource(id = R.string.skip)
        ),
    )

    val pagerState = rememberPagerState(
        pageCount = items.size,
        initialOffScreenLimit = 1,
    )
    HorizontalPager(
        state = pagerState, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) { page ->
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = items[page].painter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )

        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = dimensionResource(id = R.dimen.size_20dp),
                    end = dimensionResource(id = R.dimen.size_20dp)
                )
        ) {

            AppText(
                text = items[page].text,
                color = SkipColor,
                fontSize = dimensionResource(
                    id = R.dimen.txt_size_14
                ).value.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.clickable {
                    onNextScreen()
                }
            )

        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = dimensionResource(id = R.dimen.size_20dp))
    ) {


        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = dimensionResource(id = R.dimen.size_5dp))
        ) {
            BottomSection(items.size, pagerState.currentPage, onNextScreen) {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        }

    }
}


@Composable
fun BottomSection(size: Int, currentPager: Int, onNextScreen: () -> Unit, onNextPage: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(modifier = Modifier.weight(1.5F)) {
            PagerIndicator(
                size = size,
                currentPage = currentPager,
            )
        }

        if (currentPager == size - 1) {
            SingleWalkthroughButtonView(
                text = stringResource(id = R.string.let_go),
                painter = painterResource(id = R.drawable.ic_circle_next),
                color = Color.Black,
                iconColor = ColorGreyLighter,
                onClick = {
                    onNextScreen()
                },
                backgroundColor = Color.White
            )
        } else {
            SingleWalkthroughButtonView(
                text = stringResource(id = R.string.next),
                painter = painterResource(id = R.drawable.ic_circle_next),
                color = Color.White,
                iconColor = Color.White,
                onClick = {
                    onNextPage()
                },
                backgroundColor = Color.Black
            )
        }

    }
}


