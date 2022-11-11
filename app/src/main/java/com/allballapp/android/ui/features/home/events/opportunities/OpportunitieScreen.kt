package com.allballapp.android.ui.features.home.events.opportunities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.allballapp.android.R
import com.allballapp.android.ui.features.components.CoilImage
import com.allballapp.android.ui.features.components.CommonProgressBar
import com.allballapp.android.ui.features.components.PlaceholderRect
import com.allballapp.android.ui.features.home.events.EvEvents
import com.allballapp.android.ui.features.home.events.EventViewModel
import com.allballapp.android.ui.features.home.events.OpportunitiesItem
import com.allballapp.android.ui.theme.appColors
import com.allballapp.android.ui.utils.CommonUtils

@Composable
fun OpportunitiesScreen(vm: EventViewModel, moveToOppDetails: (String) -> Unit) {

    val state = vm.eventState.value
    remember {
        vm.onEvent(EvEvents.GetOpportunities)
    }
    if (state.isLoading) {
        CommonProgressBar()
    } else if (state.opportunitiesList.isNotEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(
                        top = dimensionResource(id = R.dimen.size_16dp),
                        start = dimensionResource(id = R.dimen.size_16dp),
                        end = dimensionResource(id = R.dimen.size_16dp)
                    )
            ) {
                LazyColumn(Modifier.fillMaxWidth()) {
                    items(state.opportunitiesList) { leag ->
                        OpportunitiesItem(leag, true) {
                            vm.onEvent(EvEvents.SetEventId(leag.id))
                            moveToOppDetails(leag.name)
                        }
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.ic_events_large),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_30dp)))
                Text(
                    color = MaterialTheme.appColors.textField.label,
                    text = stringResource(id = R.string.no_opportunities),
                    fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                    fontWeight = FontWeight.W600
                )
            }
        }
    }

}

@Composable
fun OpportunitiesItem(league: OpportunitiesItem, showLabel: Boolean, OnNextClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                OnNextClick()
            }
            .padding(bottom = dimensionResource(id = R.dimen.size_12dp))
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.appColors.material.surface,
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                )
                .padding(
                    dimensionResource(id = R.dimen.size_12dp)
                ),
        ) {
            CoilImage(
                src = com.allballapp.android.BuildConfig.IMAGE_SERVER + league.logo,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .background(
                        color = MaterialTheme.appColors.material.primary,
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                    )
                    .size(dimensionResource(id = R.dimen.size_64dp))
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
                isCrossFadeEnabled = false,
                onLoading = { PlaceholderRect(R.drawable.ic_events_placeholder) },
                onError = { PlaceholderRect(R.drawable.ic_events_placeholder) }
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = league.name,
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1F)
                    )

                    Row(
                        modifier = Modifier.weight(0.8F),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                                .background(MaterialTheme.appColors.material.primaryVariant)
                                .padding(
                                    horizontal = dimensionResource(id = R.dimen.size_12dp),
                                    vertical = dimensionResource(
                                        id = R.dimen.size_6dp
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = league.participation.type.capitalize() + " " + league.participation.minRange + "-" + league.participation.maxRange,
                                color = Color.White,
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
                Text(
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.size_100dp)),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    text = league.eventShortDescription,
                    color = Color.Black,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    fontWeight = FontWeight.W400,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_6dp)))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = CommonUtils.formatDate(league.startDate, league.endDate),
                        color = MaterialTheme.appColors.textField.label,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        fontWeight = FontWeight.W500,
                    )

                    if (showLabel)
                        Text(
                            text = "$ " + league.standardPrice,
                            color = MaterialTheme.appColors.textField.label,
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            fontWeight = FontWeight.W500,
                            textAlign = TextAlign.End
                        )
                }
            }
        }

    }
}
