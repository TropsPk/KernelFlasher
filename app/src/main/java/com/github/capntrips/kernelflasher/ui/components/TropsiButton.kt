package com.github.capntrips.kernelflasher.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TropsiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    emphasized: Boolean = false
) {
    val shape = RoundedCornerShape(14.dp)
    if (emphasized) {
        FilledTonalButton(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            onClick = onClick
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            onClick = onClick
        ) {
            Text(text)
        }
    }
}
