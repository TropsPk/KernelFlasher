package com.github.capntrips.kernelflasher.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.github.capntrips.kernelflasher.R

/** Angular display face (Rajdhani, SIL OFL) that matches the Tropsi X banner lettering. */
val DisplayFont = FontFamily(
    Font(R.font.rajdhani_medium, FontWeight.Medium),
    Font(R.font.rajdhani_semibold, FontWeight.SemiBold),
    Font(R.font.rajdhani_bold, FontWeight.Bold)
)

val Typography = Typography(
    headlineSmall = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.4.sp
    ),
    titleMedium = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.3.sp
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelLarge = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.6.sp
    ),
    labelMedium = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.6.sp
    )
)
