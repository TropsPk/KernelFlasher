package com.github.capntrips.kernelflasher.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.github.capntrips.kernelflasher.R
import com.github.capntrips.kernelflasher.ui.theme.Neon
import com.github.capntrips.kernelflasher.ui.theme.RedBright
import com.github.capntrips.kernelflasher.ui.theme.RedCore
import com.github.capntrips.kernelflasher.ui.theme.RimBorder
import com.github.capntrips.kernelflasher.ui.theme.TropsiCardShape

/** Brand card for the main screen: tagline on the left, the cat emerging from the glow on the right. */
@Composable
fun HeroCard() {
    val tagline = stringResource(R.string.tagline).trim()
    val lead = tagline.substringBeforeLast(' ', "")
    val last = tagline.substringAfterLast(' ')
    Surface(
        shape = TropsiCardShape,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = RimBorder
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(168.dp)
                .drawBehind {
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(RedCore.copy(alpha = 0.42f), Color.Transparent),
                            center = Offset(size.width * 0.80f, size.height * 0.45f),
                            radius = size.width * 0.55f
                        )
                    )
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(Neon.copy(alpha = 0.20f), Color.Transparent),
                            center = Offset(size.width * 0.58f, size.height * 0.85f),
                            radius = size.width * 0.35f
                        )
                    )
                }
        ) {
            Image(
                painter = painterResource(R.drawable.ic_brand_hero),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(164.dp)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth(0.56f)
                    .fillMaxHeight()
                    .padding(start = 20.dp, top = 18.dp, bottom = 18.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "TROPSI  X",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = buildAnnotatedString {
                        if (lead.isNotEmpty()) append("$lead ")
                        withStyle(SpanStyle(color = RedBright)) { append(last) }
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}
