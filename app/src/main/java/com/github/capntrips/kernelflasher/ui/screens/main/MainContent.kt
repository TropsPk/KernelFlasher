package com.github.capntrips.kernelflasher.ui.screens.main

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.capntrips.kernelflasher.R
import com.github.capntrips.kernelflasher.ui.components.DataCard
import com.github.capntrips.kernelflasher.ui.components.DataRow
import com.github.capntrips.kernelflasher.ui.components.SlotCard
import com.github.capntrips.kernelflasher.ui.components.TropsiButton
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalMaterial3Api
@ExperimentalSerializationApi
@Composable
fun ColumnScope.MainContent(
    viewModel: MainViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    DataCard (title = stringResource(R.string.device)) {
        val cardWidth = remember { mutableIntStateOf(0) }
        DataRow(stringResource(R.string.model), "${Build.MODEL} (${Build.DEVICE})", mutableMaxWidth = cardWidth)
        DataRow(stringResource(R.string.build_number), Build.ID, mutableMaxWidth = cardWidth)
        DataRow(stringResource(R.string.kernel_version), viewModel.kernelVersion, mutableMaxWidth = cardWidth, clickable = true)
        if (viewModel.isAb)
            DataRow(stringResource(R.string.slot_suffix), viewModel.slotSuffix, mutableMaxWidth = cardWidth)

        if(viewModel.susfsVersion != "v0.0.0" && viewModel.susfsVersion != "Invalid")
            DataRow(stringResource(R.string.susfs_version), viewModel.susfsVersion, mutableMaxWidth = cardWidth)

        if(viewModel.halInfo != "")
            DataRow("Boot HAL version", viewModel.halInfo, mutableMaxWidth = cardWidth)
    }
    Spacer(Modifier.height(16.dp))
    SlotCard(
        title = stringResource(if (viewModel.isAb) R.string.slot_a else R.string.slot),
        viewModel = viewModel.slotA,
        navController = navController
    )
    if (viewModel.isAb) {
        Spacer(Modifier.height(16.dp))
        SlotCard(
            title = stringResource(R.string.slot_b),
            viewModel = viewModel.slotB!!,
            navController = navController
        )
    }
    Spacer(Modifier.height(16.dp))
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AnimatedVisibility(!viewModel.isRefreshing) {
            TropsiButton(
                text = stringResource(R.string.backups),
                emphasized = true,
                onClick = { navController.navigate("backups") }
            )
        }
        AnimatedVisibility(!viewModel.isRefreshing) {
            TropsiButton(
                text = stringResource(R.string.updates),
                emphasized = true,
                onClick = { navController.navigate("updates") }
            )
        }
        if (viewModel.hasRamoops) {
            TropsiButton(
                text = stringResource(R.string.save_ramoops),
                onClick = { viewModel.saveRamoops(context) }
            )
        }
        TropsiButton(
            text = stringResource(R.string.save_dmesg),
            onClick = { viewModel.saveDmesg(context) }
        )
        TropsiButton(
            text = stringResource(R.string.save_logcat),
            onClick = { viewModel.saveLogcat(context) }
        )
        AnimatedVisibility(!viewModel.isRefreshing) {
            TropsiButton(
                text = stringResource(R.string.reboot),
                onClick = { navController.navigate("reboot") }
            )
        }
    }
}
