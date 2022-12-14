package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CoilImage(
    src: Any,
    modifier: Modifier,
    isCrossFadeEnabled: Boolean = true,
    onError: @Composable (() -> Unit)? = null,
    onLoading: @Composable (() -> Unit)? = null,
) {
    val painter =
        rememberAsyncImagePainter(ImageRequest.Builder(LocalContext.current).data(src).apply(block = fun ImageRequest.Builder.() {
            memoryCachePolicy(policy = CachePolicy.ENABLED)
        }).build())
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Image(
            contentScale = ContentScale.None,
            painter = painter,
            modifier = modifier,
            contentDescription = null,
        )
        when (val state = painter.state) {
            is AsyncImagePainter.State.Loading -> onLoading?.invoke()
            is AsyncImagePainter.State.Success -> Unit
            is AsyncImagePainter.State.Error, is AsyncImagePainter.State.Empty -> onError?.invoke()
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CoilImageBox(
    src: Any,
    modifier: Modifier,
    isCrossFadeEnabled: Boolean = true,
    onError: @Composable (() -> Unit)? = null,
    onLoading: @Composable (() -> Unit)? = null,
) {
    val painter =
        rememberAsyncImagePainter(ImageRequest.Builder(LocalContext.current).data(src).apply(block = fun ImageRequest.Builder.() {
            memoryCachePolicy(policy = CachePolicy.ENABLED)
        }).build())
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Image(
            contentScale = ContentScale.None,
            painter = painter,
            modifier = modifier,
            contentDescription = null,
        )
        when (val state = painter.state) {
            is AsyncImagePainter.State.Loading -> onLoading?.invoke()
            is AsyncImagePainter.State.Success -> Unit
            is AsyncImagePainter.State.Error, is AsyncImagePainter.State.Empty -> onError?.invoke()
        }
    }
}

@Composable
fun Placeholder(id: Int) {
    Surface(color = ColorBWGrayLight, shape = CircleShape) {
        Image(
            painter = painterResource(id),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun PlaceholderRect(id: Int) {
    Surface(color = ColorBWGrayLight, shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_10dp))) {
        Image(
            painter = painterResource(id),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )
    }
}