package com.github.capntrips.kernelflasher.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.capntrips.kernelflasher.ui.theme.RimBorder
import com.github.capntrips.kernelflasher.ui.theme.TropsiCardShape

// TODO: Remove when card is supported in material3: https://m3.material.io/components/cards/implementation/android
@Composable
fun Card(
    shape: Shape = TropsiCardShape,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    border: BorderStroke? = RimBorder,
    tonalElevation: Dp = 0.dp,
    shadowElevation: Dp = 0.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.animateContentSize(),
        shape = shape,
        color = backgroundColor,
        contentColor = contentColor,
        border = border,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp, 14.dp, 18.dp, 18.dp),
            content = content
        )
    }
}
