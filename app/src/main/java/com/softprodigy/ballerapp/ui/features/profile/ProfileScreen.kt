package com.softprodigy.ballerapp.ui.features.profile


import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.android.gms.common.api.ApiException
import com.softprodigy.ballerapp.LocalFacebookCallbackManager
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.CustomFBManager
import com.softprodigy.ballerapp.common.FacebookUserProfile
import com.softprodigy.ballerapp.common.GoogleApiContract
import com.softprodigy.ballerapp.common.RequestCode
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.common.isValidPassword
import com.softprodigy.ballerapp.common.passwordMatches
import com.softprodigy.ballerapp.data.SocialUserModel
import com.softprodigy.ballerapp.data.request.SignUpData
import com.softprodigy.ballerapp.data.response.Team
import com.softprodigy.ballerapp.data.response.UserInfo
import com.softprodigy.ballerapp.ui.features.components.*
import androidx.compose.runtime.*
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.softprodigy.ballerapp.ui.theme.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.util.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
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
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {
                                onBackClick()
                            },
                    )
                    Box(  modifier = Modifier.fillMaxWidth(),
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




