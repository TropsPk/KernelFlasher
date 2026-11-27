package com.github.capntrips.kernelflasher.ui.components

import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.github.capntrips.kernelflasher.MainActivity
import com.github.capntrips.kernelflasher.common.PartitionUtil.CustomSlotTarget
import com.github.capntrips.kernelflasher.ui.theme.TropsiShape
import kotlinx.serialization.ExperimentalSerializationApi

private const val CUSTOM_FLASH_HELP =
    "What it does\n" +
    "Flashes any .img file to a partition that is not in the normal list, for example dtb or logo.\n\n" +
    "How to use it\n" +
    "1. Tap Custom Partition and type the partition name without _a or _b. " +
    "The slot you are viewing is added for you.\n" +
    "2. Under Flash to, Simple Flash is ticked by default. It flashes the current slot (the slot you opened). " +
    "You can also pick Partition A only, Partition B only, or Both A and B.\n" +
    "3. Recommended: turn on Back up this partition first.\n" +
    "4. Choose your .img file. It must be a raw image, not a sparse image.\n" +
    "5. Read the confirm screen. It shows the exact partitions. If anything looks wrong, cancel.\n" +
    "6. After flashing, check the log. It says Verified when the written data matches your image.\n\n" +
    "Both A and B\n" +
    "All checks and backups run first. Then A is flashed and verified, then B. " +
    "If A fails, B is not touched.\n\n" +
    "Safety\n" +
    "- The wrong image on the wrong partition can stop the phone from booting.\n" +
    "- Protected partitions such as xbl, abl, tz, hyp, persist, userdata and super are blocked.\n" +
    "- Only real partitions are accepted. Logical partitions such as system and vendor are not supported here.\n" +
    "- Backups are saved in /sdcard/KernelFlasher/custom_backups."

/** Small round "?" mark. Opens the help dialog when tapped. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HelpMark(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .size(36.dp)
            .semantics { contentDescription = "Help" },
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text("?", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

/**
 * "Custom Partition" button for the Flash Partition Image screen.
 * Asks for a partition name, then opens the file picker, then calls [onPicked].
 * The caller is responsible for validating the partition and confirming with the user.
 */
@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalUnitApi::class,
    ExperimentalSerializationApi::class
)
@Composable
fun CustomFlashButton(
    slotSuffix: String,
    onPicked: (uri: Uri, displayName: String, partitionName: String, backupFirst: Boolean, target: CustomSlotTarget) -> Unit
) {
    val mainActivity = LocalContext.current as MainActivity
    var showHelp by remember { mutableStateOf(false) }
    var showNameDialog by remember { mutableStateOf(false) }
    var partitionName by remember { mutableStateOf("") }
    var backupFirst by remember { mutableStateOf(false) }
    var target by remember { mutableStateOf(CustomSlotTarget.CURRENT) }
    val isAb = slotSuffix.isNotEmpty()
    var nameError by remember { mutableStateOf<String?>(null) }
    // A new timestamp per pick, so picking the same file twice still triggers.
    val picked = remember { mutableStateOf<Pair<Long, Uri>?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri == null) {
            mainActivity.isAwaitingResult = false
        } else {
            picked.value = Pair(System.nanoTime(), uri)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TropsiButton(
            modifier = Modifier.weight(1f),
            shape = TropsiShape,
            onClick = {
                target = CustomSlotTarget.CURRENT
                showNameDialog = true
            }
        ) {
            Text("Custom Partition")
        }
    }

    picked.value?.let { pick ->
        val uri = pick.second
        // Use each pick once, so an old file is never reused for a new partition.
        picked.value = null
        if (mainActivity.isAwaitingResult) {
            val fileName = mainActivity.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && cursor.moveToFirst()) cursor.getString(nameIndex) else null
            }
            if (fileName != null && fileName.endsWith(".img", ignoreCase = true)) {
                onPicked(uri, fileName, partitionName.trim(), backupFirst, if (isAb) target else CustomSlotTarget.CURRENT)
            } else {
                Toast.makeText(mainActivity.applicationContext, "Invalid file selected! Choose a .img file", Toast.LENGTH_LONG).show()
            }
        }
        mainActivity.isAwaitingResult = false
    }

    if (showNameDialog) {
        AlertDialog(
            onDismissRequest = { showNameDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Custom Partition", Modifier.weight(1f))
                    Spacer(Modifier.width(8.dp))
                    HelpMark { showHelp = true }
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Type the partition name without _a or _b. Example: dtb")
                    OutlinedTextField(
                        value = partitionName,
                        onValueChange = { input ->
                            partitionName = input
                                .filter { it in 'a'..'z' || it in 'A'..'Z' || it in '0'..'9' || it == '_' }
                                .take(40)
                            nameError = null
                        },
                        label = { Text("Partition name") },
                        singleLine = true,
                        isError = nameError != null
                    )
                    val currentError = nameError
                    if (currentError != null) {
                        Text(currentError, color = MaterialTheme.colorScheme.error)
                    }
                    Text("Flash to", fontWeight = FontWeight.Bold)
                    val currentLabel = "Simple Flash"
                    val options = if (isAb) listOf(
                        CustomSlotTarget.CURRENT to currentLabel,
                        CustomSlotTarget.SLOT_A to "Partition A only",
                        CustomSlotTarget.SLOT_B to "Partition B only",
                        CustomSlotTarget.BOTH to "Both A and B"
                    ) else listOf(CustomSlotTarget.CURRENT to currentLabel)
                    for ((option, label) in options) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { target = option },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = target == option, onCheckedChange = null)
                            Spacer(Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { backupFirst = !backupFirst },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = backupFirst, onCheckedChange = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Back up the partition first (recommended)")
                    }
                }
            },
            confirmButton = {
                DialogButton("Choose Image") {
                    val name = partitionName.trim()
                    if (name.isEmpty()) {
                        nameError = "Type a partition name first"
                    } else {
                        nameError = null
                        showNameDialog = false
                        mainActivity.isAwaitingResult = true
                        launcher.launch("*/*")
                    }
                }
            },
            dismissButton = {
                DialogButton("Cancel") { showNameDialog = false }
            }
        )
    }

    if (showHelp) {
        AlertDialog(
            onDismissRequest = { showHelp = false },
            title = { Text("Custom Partition Flash") },
            text = {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    Text(CUSTOM_FLASH_HELP)
                }
            },
            confirmButton = {
                DialogButton("OK") { showHelp = false }
            }
        )
    }
}
