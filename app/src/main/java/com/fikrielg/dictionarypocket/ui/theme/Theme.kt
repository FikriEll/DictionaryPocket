package com.fikrielg.dictionarypocket.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

val DarkThemeColorSchema = darkColorScheme(
    primary = Primary02,
    onPrimary = onBackground02,
    background = Background02,
    onBackground = onBackground02,
    surface = Surface02,
    onSurface = OnSurface02,
    surfaceVariant = SurfaceVariant02,
    onSurfaceVariant = OnSurfaceVariant02,
    tertiary = Tertiary02,
    onTertiary = Tertiary02,
    errorContainer = ErrorContainer02,
    onErrorContainer = OnErrorContainer02,
    secondary = Secondary02
)
val LightThemeColorSchema = lightColorScheme(
    primary = Primary01,
    onPrimary = onBackground01,
    background = Background01,
    onBackground = onBackground01,
    surface = Surface01,
    onSurface = OnSurface01,
    surfaceVariant = SurfaceVariant01,
    onSurfaceVariant = OnSurfaceVariant01,
    tertiary = Tertiary01,
    onTertiary = Tertiary01,
    errorContainer = ErrorContainer01,
    onErrorContainer = OnErrorContainer01,
    secondary = Secondary01

)

//@Composable
//fun DictionaryPocketTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
//    content: @Composable () -> Unit
//) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.primary.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
//        }
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography,
//        content = content
//    )
//}

@Composable
fun DictionaryPocketTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme){
        DarkThemeColorSchema
    }else {
        LightThemeColorSchema
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}