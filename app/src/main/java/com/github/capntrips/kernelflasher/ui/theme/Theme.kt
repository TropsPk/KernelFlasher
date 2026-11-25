package com.github.capntrips.kernelflasher.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val TropsiColorScheme = darkColorScheme(
    primary = RedBright,
    onPrimary = Color(0xFF230003),
    primaryContainer = RedDeep,
    onPrimaryContainer = RedSoft,
    inversePrimary = RedCore,
    secondary = Neon,
    onSecondary = Color(0xFF00131F),
    secondaryContainer = NeonDeep,
    onSecondaryContainer = NeonSoft,
    tertiary = Chrome,
    onTertiary = Void,
    tertiaryContainer = PanelHighest,
    onTertiaryContainer = Chrome,
    background = Void,
    onBackground = Chrome,
    surface = Void,
    onSurface = Chrome,
    surfaceVariant = PanelHigh,
    onSurfaceVariant = Smoke,
    surfaceTint = Color.Transparent,
    inverseSurface = Chrome,
    inverseOnSurface = Void,
    error = Color(0xFFFF8A80),
    onError = Color(0xFF3B0A06),
    errorContainer = Color(0xFF5C1410),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = RedOutline,
    outlineVariant = Hairline,
    scrim = Void,
    surfaceBright = PanelHighest,
    surfaceDim = Void,
    surfaceContainerLowest = Void,
    surfaceContainerLow = Color(0xFF0A0A10),
    surfaceContainer = Panel,
    surfaceContainerHigh = PanelHigh,
    surfaceContainerHighest = PanelHighest
)

private val TropsiShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

/** Corner shape for buttons. */
val TropsiShape = RoundedCornerShape(14.dp)

/** Corner shape for cards. */
val TropsiCardShape = RoundedCornerShape(18.dp)

/** Thin blue (left) to red (right) rim light, like the edges of the logo. */
val RimBorder = BorderStroke(
    1.dp,
    Brush.horizontalGradient(
        listOf(
            Neon.copy(alpha = 0.55f),
            Hairline,
            RedBright.copy(alpha = 0.60f)
        )
    )
)

// The app has one look: dark Tropsi X. The parameters are kept so existing callers keep compiling.
@Suppress("UNUSED_PARAMETER")
@Composable
fun KernelFlasherTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TropsiColorScheme,
        typography = Typography,
        shapes = TropsiShapes,
        content = content
    )
}
