package com.softprodigy.ballerapp.ui.features.profile


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.*
import androidx.compose.runtime.*
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.softprodigy.ballerapp.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    moveToEditProfile:()->Unit
) {
    val pagerState = rememberPagerState(pageCount = 2)
    var selectedTabLabel by rememberSaveable { mutableStateOf(TabItems.Events.stringId) }

    Column(
        modifier = Modifier.background(MaterialTheme.appColors.material.background)
    ) {
        TopAppBar(backgroundColor = ColorMainPrimary) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {
                                onBackClick()
                            },
                    )
                    Box(  modifier = Modifier.weight(1f),
                        )
                    {
                        AppText(
                            text =  stringResourceByName(selectedTabLabel),
                            style = MaterialTheme.typography.h6,
                            color = heading2OnDarkColor,
                            fontSize = dimensionResource(id = R.dimen.txt_size_20).value.sp,
                            modifier=Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {
                                moveToEditProfile()
                            },
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))

                }
            }
        }
        Tabs(pagerState = pagerState,changedHeading={
            selectedTabLabel=it
        })
        TabsContent(pagerState = pagerState)
    }
}

@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState,changedHeading:(heading:String)->Unit) {
    val list = listOf(
        TabItems.Events,
        TabItems.Leagues,
    )
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.White,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = ColorMainPrimary
            )
        }
    ) {
        list.forEachIndexed { index, item ->
            Tab(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = null,
                            tint = if (pagerState.currentPage == index) MaterialTheme.appColors.material.primaryVariant else MaterialTheme.appColors.textField.label
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_5dp)))
                        Text(
                            stringResourceByName(name = item.stringId),
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            color = if (pagerState.currentPage == index) MaterialTheme.appColors.material.primaryVariant else MaterialTheme.appColors.textField.label
                        )
                    }
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                        changedHeading(list[index].stringId)
                    }
                }
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState) {
    HorizontalPager(state = pagerState) {
            page ->
        when (page) {
            0 -> ProfileTab()
            1 -> DocumentTab()
        }
    }
}
enum class TabItems(val icon: Int, val stringId: String) {
    Events(R.drawable.ic_profile, stringId = "profile"),
    Leagues(R.drawable.ic_documents, stringId = "documents"),
}




