package com.github.capntrips.kernelflasher.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.capntrips.kernelflasher.ui.theme.RedBright
import com.github.capntrips.kernelflasher.ui.theme.RedDeep

@Composable
fun StatusPill(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier
            .clip(CircleShape)
            .background(RedDeep.copy(alpha = 0.55f))
            .border(1.dp, RedBright.copy(alpha = 0.6f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.labelMedium,
        maxLines = 1
    )
}
