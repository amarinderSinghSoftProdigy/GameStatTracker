package com.softprodigy.ballerapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.softprodigy.ballerapp.common.AppConstants

data class EditField(
    val borderFocused: Color,
    val borderUnFocused: Color,
    val borderError: Color,
)

data class TextFieldColor(
    val label: Color,
    val indicator: Color
)

data class SpinnerColor(
    val popupBackground: Color,
    val popupBorder: Color
)

data class ButtonColor(
    val bckgroundEnabled: Color,
    val bckgroundDisabled: Color,
    val textEnabled: Color,
    val textDisabled: Color
)

data class appColors(
    val material: Colors,
    val warning: Color,
    val onWarning: Color,
    val editField: EditField,
    val textField: TextFieldColor,
    val spinnerColo: SpinnerColor,
    val buttonColor: ButtonColor

)

private val LightThemeColors = lightColors(
    primary = md_theme_light_primary, // Will be used for the base background
    primaryVariant = md_theme_light_primaryContainer,//Will be used for Selected theme color.
    onPrimary = md_theme_light_onPrimary,
    secondary = md_theme_light_secondary,
    secondaryVariant = md_theme_light_secondaryContainer,
    onSecondary = md_theme_light_onSecondary,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
)

private val DarkThemeColors = darkColors(
    primary = md_theme_dark_primary,
    primaryVariant = md_theme_dark_primaryContainer,
    onPrimary = md_theme_dark_onPrimary,
    secondary = md_theme_dark_secondary,
    secondaryVariant = md_theme_dark_secondaryContainer,
    onSecondary = md_theme_dark_onSecondary,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface
)

private val LightColorPalette = appColors(
    material = LightThemeColors,
    warning = error,
    onWarning = Color.Black,
    editField = EditField(
        borderFocused = ol_field_border_focused,
        borderUnFocused = ol_field_border_unfocused,
        borderError = ol_field_border_error
    ),
    textField = TextFieldColor(
        label = text_field_label,
        indicator = text_field_indicator
    ),
    spinnerColo = SpinnerColor(
        popupBackground = spiner_popup_background,
        popupBorder = spiner_popup_border
    ),
    buttonColor = ButtonColor(
        bckgroundDisabled = button_background_disable,
        bckgroundEnabled = button_background_enable,
        textEnabled = button_text_enable,
        textDisabled = button_text_disable
    )

)

private val DarkColorPalette = LightColorPalette

private val LocalColors = staticCompositionLocalOf { LightColorPalette }

@Composable
fun BallerAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    val sysUiController = rememberSystemUiController()
    SideEffect {
        sysUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = !darkTheme
        )
    }

    CompositionLocalProvider(LocalColors provides colors) {
        MaterialTheme(
            colors = colors.material,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

@Composable
fun BallerAppMainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette.copy(material = LightThemeColors.copy(primaryVariant = AppConstants.SELECTED_COLOR))
    } else {
        LightColorPalette.copy(material = LightThemeColors.copy(primaryVariant = AppConstants.SELECTED_COLOR))
    }
    val sysUiController = rememberSystemUiController()
    SideEffect {
        sysUiController.setStatusBarColor(
            color = colors.material.primaryVariant,
            darkIcons = !darkTheme
        )
    }

    CompositionLocalProvider(LocalColors provides colors) {
        MaterialTheme(
            colors = colors.material,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

val MaterialTheme.appColors: appColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current

