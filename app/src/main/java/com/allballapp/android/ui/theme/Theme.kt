package com.allballapp.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.allballapp.android.common.AppConstants

data class EditField(
    val borderFocused: Color,
    val borderUnFocused: Color,
    val borderError: Color,
)

data class TextFieldColor(
    val label: Color,
    val labelColor: Color,
    val labelDark: Color,
    val indicator: Color
)

data class SpinnerColor(
    val popupBackground: Color,
    val popupBorder: Color
)

data class ButtonColor(
    val backgroundEnabled: Color,
    val backgroundDisabled: Color,
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
    onSurface = md_theme_dark_onSurface,

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
        labelColor = ColorBWBlack,
        labelDark = text_field_label_dark,
        indicator = text_field_indicator,
    ),
    spinnerColo = SpinnerColor(
        popupBackground = spiner_popup_background,
        popupBorder = spiner_popup_border
    ),
    buttonColor = ButtonColor(
        backgroundDisabled = button_background_disable,
        backgroundEnabled = button_background_enable,
        textEnabled = button_text_enable,
        textDisabled = button_text_disable
    )

)

private val DarkColorPalette = appColors(
    material = LightThemeColors,
    warning = error,
    onWarning = Color.Black,
    editField = EditField(
        borderFocused = ol_field_border_focused,
        borderUnFocused = ol_field_border_unfocused,
        borderError = ol_field_border_error
    ),
    textField = TextFieldColor(
        label = text_field_label_dark_theme,
        labelColor = /*ColorBWBlack*/ text_field_label_dark_theme,
        labelDark = text_field_label_dark,
        indicator = text_field_indicator,
    ),
    spinnerColo = SpinnerColor(
        popupBackground = spiner_popup_background,
        popupBorder = spiner_popup_border
    ),
    buttonColor = ButtonColor(
        backgroundDisabled = button_background_disable,
        backgroundEnabled = button_background_enable_dark_theme,
        textEnabled = button_text_enable,
        textDisabled = button_text_disable
    )

)

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
            color = if (!darkTheme) md_theme_light_primary else ColorBWGrayStatus,
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
    darkTheme: Boolean = /*false*/ isSystemInDarkTheme(),
    customColor: Color = AppConstants.SELECTED_COLOR,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette.copy(material = DarkThemeColors.copy(primaryVariant = customColor))
    } else {
        LightColorPalette.copy(material = LightThemeColors.copy(primaryVariant = customColor))
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

