package com.softprodigy.ballerapp.ui.features.profile.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.profile.ProfileViewModel
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight

@Composable
fun RefereePayTab(vm: ProfileViewModel) {

    val state = vm.state.value

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = dimensionResource(id = R.dimen.size_16dp))
        ) {
            AppText(
                text = stringResource(id = R.string.pay_stats), style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W500,
                color = ColorBWBlack
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {

                itemsIndexed(state.payData) { index, item ->

                    RefereePayItem(name = item.name, price = item.price, date = item.date)

                }

            }

        }


    }

}

@Composable
fun RefereePayItem(name: String, price: String, date: String) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {

            AppText(text = name, style = MaterialTheme.typography.h6, color = ColorBWBlack)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            AppText(text = date, style = MaterialTheme.typography.h4, color = ColorBWGrayLight)

        }
        AppText(text = price, style = MaterialTheme.typography.h5, color = ColorBWBlack)

    }

    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_25dp)))

}