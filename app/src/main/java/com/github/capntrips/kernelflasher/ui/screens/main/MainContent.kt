package com.github.capntrips.kernelflasher.ui.screens.main

import com.github.capntrips.kernelflasher.ui.components.TropsiButton

import com.github.capntrips.kernelflasher.ui.theme.TropsiShape

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import com.github.capntrips.kernelflasher.ui.components.HeroCard
import com.github.capntrips.kernelflasher.ui.components.SlotCard
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalMaterial3Api
@ExperimentalSerializationApi
@Composable
fun ColumnScope.MainContent(
    viewModel: MainViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    HeroCard()
    Spacer(Modifier.height(16.dp))
    DataCard (title = stringResource(R.string.device)) {
        val cardWidth = remember { mutableIntStateOf(0) }
        DataRow(stringResource(R.string.model), "${Build.MODEL} (${Build.DEVICE})", mutableMaxWidth = cardWidth)
        DataRow(stringResource(R.string.build_number), Build.ID, mutableMaxWidth = cardWidth)
        DataRow(stringResource(R.string.kernel_version), viewModel.kernelVersion, mutableMaxWidth = cardWidth, clickable = true)
        if (viewModel.isAb)
            DataRow(stringResource(R.string.slot_suffix), viewModel.slotSuffix, mutableMaxWidth = cardWidth)

        if(viewModel.susfsVersion != "v0.0.0" && viewModel.susfsVersion != "Invalid")
            DataRow(stringResource(R.string.susfs_version), viewModel.susfsVersion, mutableMaxWidth = cardWidth)

        if (viewModel.isAb && viewModel.halInfo != "")
            DataRow("Boot HAL version", viewModel.halInfo, mutableMaxWidth = cardWidth)
    }
    Spacer(Modifier.height(16.dp))
    SlotCard(
        title = stringResource(if (viewModel.isAb) R.string.slot_a else R.string.partition_info),
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
    AnimatedVisibility(!viewModel.isRefreshing) {
        TropsiButton(
            modifier = Modifier
                .fillMaxWidth(),
            shape = TropsiShape,
            onClick = { navController.navigate("backups") }
        ) {
            Text(stringResource(R.string.backups))
        }
    }
    AnimatedVisibility(!viewModel.isRefreshing) {
        TropsiButton(
            modifier = Modifier
                .fillMaxWidth(),
            shape = TropsiShape,
            onClick = { navController.navigate("updates") }
        ) {
            Text(stringResource(R.string.updates))
        }
    }
    if (viewModel.hasRamoops) {
        TropsiButton(
            modifier = Modifier
                .fillMaxWidth(),
            shape = TropsiShape,
            onClick = { viewModel.saveRamoops(context) }
        ) {
            Text(stringResource(R.string.save_ramoops))
        }
    }
    TropsiButton(
        modifier = Modifier
            .fillMaxWidth(),
        shape = TropsiShape,
        onClick = { viewModel.saveDmesg(context) }
    ) {
        Text(stringResource(R.string.save_dmesg))
    }
    TropsiButton(
        modifier = Modifier
            .fillMaxWidth(),
        shape = TropsiShape,
        onClick = { viewModel.saveLogcat(context) }
    ) {
        Text(stringResource(R.string.save_logcat))
    }
    AnimatedVisibility(!viewModel.isRefreshing) {
        TropsiButton(
            modifier = Modifier
                .fillMaxWidth(),
            shape = TropsiShape,
            onClick = { navController.navigate("reboot") }
        ) {
            Text(stringResource(R.string.reboot))
        }
    }
}
