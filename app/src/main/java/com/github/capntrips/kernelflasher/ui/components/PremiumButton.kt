package com.github.capntrips.kernelflasher.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.github.capntrips.kernelflasher.ui.theme.RedBright
import com.github.capntrips.kernelflasher.ui.theme.RedCore
import com.github.capntrips.kernelflasher.ui.theme.RimBorder
import com.github.capntrips.kernelflasher.ui.theme.TropsiShape

/**
 * Premium Tropsi X button.
 * primary = glowing red gradient with a glass highlight; otherwise a dark panel with the blue-to-red rim light.
 * compact = smaller paddings and text for tight spots such as the hero card.
 */
@Composable
fun PremiumButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    primary: Boolean = true,
    compact: Boolean = false
) {
    val shape = TropsiShape
    val glow = if (primary) {
        Modifier.shadow(
            elevation = if (compact) 8.dp else 12.dp,
            shape = shape,
            ambientColor = RedCore,
            spotColor = RedCore
        )
    } else Modifier
    val fill = if (primary) {
        Modifier
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF9E0A14), RedCore, RedBright)
                )
            )
            .drawBehind {
                // glass highlight on the top half
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White.copy(alpha = 0.20f), Color.Transparent),
                        startY = 0f,
                        endY = size.height * 0.55f
                    ),
                    topLeft = Offset.Zero
                )
            }
            .border(
                BorderStroke(
                    1.dp,
                    Brush.verticalGradient(
                        listOf(Color.White.copy(alpha = 0.50f), RedBright.copy(alpha = 0.25f))
                    )
                ),
                shape
            )
    } else {
        Modifier
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .border(RimBorder, shape)
    }
    Row(
        modifier = modifier
            .then(glow)
            .clip(shape)
            .then(fill)
            .clickable(onClick = onClick)
            .heightIn(min = if (compact) 42.dp else 50.dp)
            .padding(horizontal = if (compact) 14.dp else 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = if (primary) Color.White else RedBright,
                modifier = Modifier.size(if (compact) 16.dp else 20.dp)
            )
            Spacer(Modifier.width(if (compact) 6.dp else 8.dp))
        }
        Text(
            text = text,
            color = if (primary) Color.White else MaterialTheme.colorScheme.onSurface,
            style = if (compact) MaterialTheme.typography.labelMedium else MaterialTheme.typography.labelLarge,
            maxLines = 1
        )
    }
}
