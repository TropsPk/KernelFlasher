package com.github.capntrips.kernelflasher.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.github.capntrips.kernelflasher.ui.theme.RimBorder
import com.github.capntrips.kernelflasher.ui.theme.TropsiShape

/** Tropsi X button: dark panel with the blue-to-red rim light. Drop-in for OutlinedButton. */
@Composable
fun TropsiButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = TropsiShape,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ),
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.heightIn(min = 52.dp),
        enabled = enabled,
        shape = shape,
        colors = colors,
        border = if (enabled) RimBorder else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        content = content
    )
}
