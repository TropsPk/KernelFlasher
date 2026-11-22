package com.github.capntrips.kernelflasher.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val TropsiDark = darkColorScheme(
    primary = Tide,
    onPrimary = TideDeep,
    primaryContainer = TideContainer,
    onPrimaryContainer = TideOnContainer,
    secondary = Color(0xFF9BC4BD),
    onSecondary = Color(0xFF05241F),
    secondaryContainer = Lagoon700,
    onSecondaryContainer = Mist,
    tertiary = Sun,
    onTertiary = SunDeep,
    tertiaryContainer = SunContainer,
    onTertiaryContainer = SunOnContainer,
    background = Lagoon950,
    onBackground = Mist,
    surface = Lagoon900,
    onSurface = Mist,
    surfaceVariant = Lagoon800,
    onSurfaceVariant = Fog,
    outline = Lagoon600,
    outlineVariant = Lagoon700,
    error = Color(0xFFFF7A72),
    onError = Color(0xFF410002),
    errorContainer = Color(0xFF5C1511),
    onErrorContainer = Color(0xFFFFDAD6)
)

private val TropsiLight = lightColorScheme(
    primary = Reef,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFA6F2DF),
    onPrimaryContainer = Color(0xFF00201B),
    secondary = Color(0xFF4A635E),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCCE8E2),
    onSecondaryContainer = Color(0xFF05201C),
    tertiary = Color(0xFF8A5100),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDDB6),
    onTertiaryContainer = Color(0xFF2C1700),
    background = Foam,
    onBackground = Color(0xFF151D1C),
    surface = Color(0xFFFAFDFC),
    onSurface = Color(0xFF151D1C),
    surfaceVariant = Color(0xFFDAE5E2),
    onSurfaceVariant = Color(0xFF3F4947),
    outline = Color(0xFF6F7977),
    outlineVariant = Color(0xFFBEC9C6)
)

private val TropsiShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun KernelFlasherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Off by default so Tropsi X colors always show. Set true to follow Material You.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> TropsiDark
        else -> TropsiLight
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = TropsiShapes,
        content = content
    )
}
