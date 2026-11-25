package com.github.capntrips.kernelflasher.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.github.capntrips.kernelflasher.R
import com.github.capntrips.kernelflasher.ui.screens.main.MainViewModel
import com.github.capntrips.kernelflasher.ui.theme.Neon
import com.github.capntrips.kernelflasher.ui.theme.RedBright
import com.github.capntrips.kernelflasher.ui.theme.RedCore
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@ExperimentalSerializationApi
@Composable
fun RefreshableScreen(
    viewModel: MainViewModel,
    navController: NavController,
    swipeEnabled: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val statusBar = WindowInsets.statusBars.only(WindowInsetsSides.Top).asPaddingValues()
    val navigationBars = WindowInsets.navigationBars.asPaddingValues()
    val context = LocalContext.current
    val state = rememberPullRefreshState(viewModel.isRefreshing, onRefresh = {
        viewModel.refresh(context)
    })
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .drawBehind {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(RedCore.copy(alpha = 0.22f), Color.Transparent),
                        center = Offset(size.width * 0.5f, 0f),
                        radius = size.width * 0.9f
                    )
                )
            }
    ) {
    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(statusBar)) {
                if (navController.previousBackStackEntry != null) {
                    AnimatedVisibility(
                        !viewModel.isRefreshing,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp)
                        ) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val appName = stringResource(R.string.app_name).uppercase()
                        val lead = appName.substringBeforeLast(' ', "")
                        val last = appName.substringAfterLast(' ')
                        Text(
                            text = buildAnnotatedString {
                                if (lead.isNotEmpty()) append("$lead ")
                                withStyle(
                                    SpanStyle(
                                        brush = Brush.horizontalGradient(
                                            listOf(Color(0xFFB80010), RedCore, RedBright, Color(0xFFFF6A5C))
                                        )
                                    )
                                ) { append(last) }
                            },
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontSize = 24.sp,
                                letterSpacing = 2.5.sp,
                                shadow = Shadow(
                                    color = RedCore.copy(alpha = 0.55f),
                                    offset = Offset.Zero,
                                    blurRadius = 24f
                                )
                            ),
                            maxLines = 1
                        )
                        Spacer(Modifier.height(6.dp))
                        Box(
                            Modifier
                                .width(140.dp)
                                .height(2.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color.Transparent, Neon, RedBright, Color.Transparent)
                                    )
                                )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .pullRefresh(state, swipeEnabled)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp, 0.dp, 16.dp, 16.dp + navigationBars.calculateBottomPadding())
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                content = content
            )
            PullRefreshIndicator(
                viewModel.isRefreshing,
                state = state,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.primary,
                scale = true
            )
        }
    }
    }
}

