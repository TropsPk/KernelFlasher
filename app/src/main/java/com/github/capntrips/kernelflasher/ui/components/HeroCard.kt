package com.github.capntrips.kernelflasher.ui.components

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.capntrips.kernelflasher.R
import com.github.capntrips.kernelflasher.ui.theme.Hairline
import com.github.capntrips.kernelflasher.ui.theme.Neon
import com.github.capntrips.kernelflasher.ui.theme.RedBright
import com.github.capntrips.kernelflasher.ui.theme.RedCore
import com.github.capntrips.kernelflasher.ui.theme.RimBorder
import com.github.capntrips.kernelflasher.ui.theme.TropsiCardShape

/** Brand card for the main screen: developer name and Support Me button on the left, the full cat on the right. */
@Composable
fun HeroCard() {
    var showSupport by remember { mutableStateOf(false) }
    val name = stringResource(R.string.developer_name)
    val lead = name.dropLast(2)
    val tail = name.takeLast(2)
    Surface(
        shape = TropsiCardShape,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = RimBorder
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(184.dp)
                .drawBehind {
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(RedCore.copy(alpha = 0.42f), Color.Transparent),
                            center = Offset(size.width * 0.78f, size.height * 0.45f),
                            radius = size.width * 0.55f
                        )
                    )
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(Neon.copy(alpha = 0.20f), Color.Transparent),
                            center = Offset(size.width * 0.56f, size.height * 0.85f),
                            radius = size.width * 0.35f
                        )
                    )
                }
        ) {
            // The whole figure is shown (ContentScale.Fit, no cropping); text gets the remaining width.
            val imageSize: Dp = minOf(176.dp, maxWidth * 0.5f)
            Image(
                painter = painterResource(R.drawable.ic_brand_hero),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(imageSize)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(maxWidth - imageSize)
                    .fillMaxHeight()
                    .padding(start = 20.dp, end = 4.dp, top = 18.dp, bottom = 18.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(lead)
                        withStyle(SpanStyle(color = RedBright)) { append(tail) }
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1
                )
                Spacer(Modifier.height(14.dp))
                PremiumButton(
                    text = stringResource(R.string.support),
                    onClick = { showSupport = true },
                    icon = painterResource(R.drawable.ic_heart),
                    compact = true
                )
            }
        }
    }
    if (showSupport) {
        SupportDialog(onDismiss = { showSupport = false })
    }
}

@Composable
private fun SupportSection(
    label: String,
    trailing: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, Hairline)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
                trailing?.invoke()
            }
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun SupportDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val address = stringResource(R.string.donation_address)
    val githubUrl = stringResource(R.string.github_url)
    val copiedText = stringResource(R.string.support_copied)
    val noBrowserText = stringResource(R.string.support_no_browser)
    val title = stringResource(R.string.support)
    val titleLead = title.substringBeforeLast(' ', "")
    val titleLast = title.substringAfterLast(' ')

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .fillMaxWidth(),
            shape = TropsiCardShape,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            border = RimBorder
        ) {
            Column(
                modifier = Modifier
                    .drawBehind {
                        drawRect(
                            brush = Brush.radialGradient(
                                colors = listOf(RedCore.copy(alpha = 0.30f), Color.Transparent),
                                center = Offset(size.width * 0.5f, 0f),
                                radius = size.width * 0.8f
                            )
                        )
                    }
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_brand_hero),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(104.dp)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = buildAnnotatedString {
                        if (titleLead.isNotEmpty()) append("$titleLead ")
                        withStyle(SpanStyle(color = RedBright)) { append(titleLast) }
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.support_subtitle),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(18.dp))

                SupportSection(
                    label = stringResource(R.string.support_donation),
                    trailing = { StatusPill(stringResource(R.string.support_usdt)) }
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.Black,
                        border = BorderStroke(1.dp, Hairline)
                    ) {
                        SelectionContainer {
                            Text(
                                text = address,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 12.dp),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.support_trc20_note),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(12.dp))
                    PremiumButton(
                        text = stringResource(R.string.support_copy_address),
                        modifier = Modifier.fillMaxWidth(),
                        icon = painterResource(R.drawable.ic_heart),
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("USDT TRC20", address))
                            Toast.makeText(context, copiedText, Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                Spacer(Modifier.height(14.dp))

                SupportSection(label = stringResource(R.string.support_github)) {
                    Text(
                        text = "github.com/TropsPk",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(12.dp))
                    PremiumButton(
                        text = stringResource(R.string.support_open_github),
                        modifier = Modifier.fillMaxWidth(),
                        primary = false,
                        onClick = {
                            try {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl))
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                )
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(context, noBrowserText, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }

                Spacer(Modifier.height(8.dp))
                DialogButton(stringResource(R.string.support_close), onDismiss)
            }
        }
    }
}
