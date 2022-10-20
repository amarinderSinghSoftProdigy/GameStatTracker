package com.softprodigy.ballerapp.ui.features.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.theme.ColorGreyLighter
import com.softprodigy.ballerapp.ui.theme.spacing
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreen(onNextScreen: () -> Unit) {

    val scope = rememberCoroutineScope()
    /*val items = arrayListOf(
        painterResource(id = R.drawable.walkthrough_1),
        painterResource(id = R.drawable.walkthrough_2),
        painterResource(id = R.drawable.walkthrough_3),
        painterResource(id = R.drawable.walkthrough_4)
    )*/

    val pagerState = rememberPagerState(
        pageCount = 5,
        initialOffScreenLimit = 2,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = dimensionResource(id = R.dimen.size_20dp))
    ) {

        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
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
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = dimensionResource(id = R.dimen.size_5dp))
        ) {
            BottomSection(pagerState.currentPage, onNextScreen) {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = dimensionResource(id = R.dimen.size_50dp),
                    end = dimensionResource(id = R.dimen.size_20dp)
                )
        ) {

            AppText(
                text = stringResource(id = R.string.skip),
                color = ColorGreyLighter,
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
}


@Composable
fun BottomSection(currentPager: Int, onNextScreen: () -> Unit, onNextPage: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(modifier = Modifier.weight(1.5F)) {
            PagerIndicator(
                size = 5,
                currentPage = currentPager,
            )
        }

        if (currentPager == 4) {
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


