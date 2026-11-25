package com.github.capntrips.kernelflasher.ui.screens.reboot

import com.github.capntrips.kernelflasher.ui.components.TropsiButton

import com.github.capntrips.kernelflasher.ui.theme.TropsiShape

import android.os.Build
import android.os.PowerManager
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.capntrips.kernelflasher.R

@Suppress("UnusedReceiverParameter")
@Composable
fun ColumnScope.RebootContent(
    viewModel: RebootViewModel,
    @Suppress("UNUSED_PARAMETER") ignoredNavController: NavController
) {
    val context = LocalContext.current
    TropsiButton(
        modifier = Modifier
            .fillMaxWidth(),
        shape = TropsiShape,
        onClick = { viewModel.rebootSystem() }
    ) {
        Text(stringResource(R.string.reboot))
    }
    TropsiButton(
        modifier = Modifier
            .fillMaxWidth(),
        shape = TropsiShape,
        onClick = { viewModel.rebootRecovery() }
    ) {
        Text(stringResource(R.string.reboot_recovery))
    }
    TropsiButton(
        modifier = Modifier
            .fillMaxWidth(),
        shape = TropsiShape,
        onClick = { viewModel.rebootBootloader() }
    ) {
        Text(stringResource(R.string.reboot_bootloader))
    }
    TropsiButton(
        modifier = Modifier
            .fillMaxWidth(),
        shape = TropsiShape,
        onClick = { viewModel.rebootDownload() }
    ) {
        Text(stringResource(R.string.reboot_download))
    }
    TropsiButton(
        modifier = Modifier
            .fillMaxWidth(),
        shape = TropsiShape,
        onClick = { viewModel.rebootEdl() }
    ) {
        Text(stringResource(R.string.reboot_edl))
    }
}
