package com.github.capntrips.kernelflasher.common

import android.content.Context
import com.github.capntrips.kernelflasher.common.extensions.ByteArray.toHex
import com.github.capntrips.kernelflasher.common.types.partitions.FstabEntry
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.nio.ExtendedFile
import com.topjohnwu.superuser.nio.FileSystemManager
import kotlinx.serialization.json.Json
import java.io.File
import java.security.DigestOutputStream
import java.security.MessageDigest

object PartitionUtil {
    val PartitionNames = listOf(
        "boot",
        "dtbo",
        "init_boot",
        "recovery",
        "system_dlkm",
        "vbmeta",
        "vendor_boot",
        "vendor_dlkm",
        "vendor_kernel_boot"
    )

    val AvailablePartitions = mutableListOf<String>()

    private var fileSystemManager: FileSystemManager? = null
    private var bootParent: File? = null

    fun init(context: Context, fileSystemManager: FileSystemManager) {
        this.fileSystemManager = fileSystemManager
        val fstabEntry = findPartitionFstabEntry(context, "boot")
        if (fstabEntry != null) {
            bootParent = File(fstabEntry.blkDevice).parentFile
        }
        val activeSlotSuffix = Shell.cmd("getprop ro.boot.slot_suffix").exec().out[0]
        for (partitionName in PartitionNames) {
            val blockDevice = findPartitionBlockDevice(context, partitionName, activeSlotSuffix)
            if (blockDevice != null && blockDevice.exists()) {
                AvailablePartitions.add(partitionName)
            }
        }
    }

    private fun findPartitionFstabEntry(context: Context, partitionName: String): FstabEntry? {
        val httools = File(context.filesDir, "httools_static")
        val result = Shell.cmd("$httools dump $partitionName").exec().out
        if (result.isNotEmpty() && result[0].trim().startsWith("{")) {
            return Json.decodeFromString<FstabEntry>(result[0])
        }
        return null
    }

    fun isPartitionLogical(context: Context, partitionName: String): Boolean {
        return findPartitionFstabEntry(context, partitionName)?.fsMgrFlags?.logical == true
    }

    fun findPartitionBlockDevice(context: Context, partitionName: String, slotSuffix: String): ExtendedFile? {
        var blockDevice: ExtendedFile? = null
        val fstabEntry = findPartitionFstabEntry(context, partitionName)
        if (fstabEntry != null) {
            if (fstabEntry.fsMgrFlags?.logical == true) {
                if (fstabEntry.logicalPartitionName == "$partitionName$slotSuffix") {
                    blockDevice = fileSystemManager!!.getFile(fstabEntry.blkDevice)
                }
            } else {
                blockDevice = fileSystemManager!!.getFile(fstabEntry.blkDevice)
                if (blockDevice.name != "$partitionName$slotSuffix") {
                    blockDevice = fileSystemManager!!.getFile(blockDevice.parentFile, "$partitionName$slotSuffix")
                }
            }
        }
        if (blockDevice == null || !blockDevice.exists()) {
            val siblingDevice = if (bootParent != null) fileSystemManager!!.getFile(bootParent!!, "$partitionName$slotSuffix") else null
            val physicalDevice = fileSystemManager!!.getFile("/dev/block/by-name/$partitionName$slotSuffix")
            val logicalDevice = fileSystemManager!!.getFile("/dev/block/mapper/$partitionName$slotSuffix")
            if (siblingDevice?.exists() == true) {
                blockDevice = physicalDevice
            } else if (physicalDevice.exists()) {
                blockDevice = physicalDevice
            } else if (logicalDevice.exists()) {
                blockDevice = logicalDevice
            }
        }
        return blockDevice
    }

    @Suppress("unused")
    fun partitionAvb(context: Context, partitionName: String): String {
        val httools = File(context.filesDir, "httools_static")
        val result = Shell.cmd("$httools avb $partitionName").exec().out
        return if (result.isNotEmpty()) result[0] else ""
    }

    fun flashBlockDevice(image: ExtendedFile, blockDevice: ExtendedFile, hashAlgorithm: String): String {
        val partitionSize = Shell.cmd("wc -c < $blockDevice").exec().out[0].toUInt()
        val imageSize = Shell.cmd("wc -c < $image").exec().out[0].toUInt()
        if (partitionSize < imageSize) {
            throw Error("Partition ${blockDevice.name} is smaller than image")
        }
        if (partitionSize > imageSize) {
            Shell.cmd("dd bs=4096 if=/dev/zero of=$blockDevice && sync").exec()
        }
        val messageDigest = MessageDigest.getInstance(hashAlgorithm)
        image.newInputStream().use { inputStream ->
            blockDevice.newOutputStream().use { outputStream ->
                DigestOutputStream(outputStream, messageDigest).use { digestOutputStream ->
                    inputStream.copyTo(digestOutputStream)
                }
            }
        }
        return messageDigest.digest().toHex()
    }

    @Suppress("SameParameterValue")
    fun flashLogicalPartition(context: Context, image: ExtendedFile, blockDevice: ExtendedFile, partitionName: String, slotSuffix: String, hashAlgorithm: String, addMessage: (message: String) -> Unit): String {
        val sourceFileSize = Shell.cmd("wc -c < $image").exec().out[0].toUInt()
        val lptools = File(context.filesDir, "lptools_static")
        Shell.cmd("$lptools remove ${partitionName}_kf").exec()
        if (Shell.cmd("$lptools create ${partitionName}_kf $sourceFileSize").exec().isSuccess) {
            if (Shell.cmd("$lptools unmap ${partitionName}_kf").exec().isSuccess) {
                if (Shell.cmd("$lptools map ${partitionName}_kf").exec().isSuccess) {
                    val temporaryBlockDevice = fileSystemManager!!.getFile("/dev/block/mapper/${partitionName}_kf")
                    val hash = flashBlockDevice(image, temporaryBlockDevice, hashAlgorithm)
                    if (Shell.cmd("$lptools replace ${partitionName}_kf $partitionName$slotSuffix").exec().isSuccess) {
                        return hash
                    } else {
                        throw Error("Replacing $partitionName$slotSuffix failed")
                    }
                } else {
                    throw Error("Remapping ${partitionName}_kf failed")
                }
            } else {
                throw Error("Unmapping ${partitionName}_kf failed")
            }
        } else {
            addMessage.invoke("Creating ${partitionName}_kf failed. Attempting to resize $partitionName$slotSuffix ...")
            val httools = File(context.filesDir, "httools_static")
            if (Shell.cmd("$httools umount $partitionName").exec().isSuccess) {
                val verityBlockDevice = blockDevice.parentFile!!.getChildFile("${partitionName}-verity")
                if (verityBlockDevice.exists()) {
                    if (!Shell.cmd("$lptools unmap ${partitionName}-verity").exec().isSuccess) {
                        throw Error("Unmapping ${partitionName}-verity failed")
                    }
                }
                if (Shell.cmd("$lptools unmap $partitionName$slotSuffix").exec().isSuccess) {
                    if (Shell.cmd("$lptools resize $partitionName$slotSuffix \$(wc -c < $image)").exec().isSuccess) {
                        if (Shell.cmd("$lptools map $partitionName$slotSuffix").exec().isSuccess) {
                            val hash = flashBlockDevice(image, blockDevice, hashAlgorithm)
                            if (Shell.cmd("$httools mount $partitionName").exec().isSuccess) {
                                return hash
                            } else {
                                throw Error("Mounting $partitionName failed")
                            }
                        } else {
                            throw Error("Remapping $partitionName$slotSuffix failed")
                        }
                    } else {
                        throw Error("Resizing $partitionName$slotSuffix failed")
                    }
                } else {
                    throw Error("Unmapping $partitionName$slotSuffix failed")
                }
            } else {
                throw Error("Unmounting $partitionName failed")
            }
        }
    }

    // ---------------------------------------------------------------------
    // Custom partition flash
    // Everything below is used only by the "Custom Partition" feature.
    // Existing functions above are intentionally left unchanged.
    // ---------------------------------------------------------------------

    /** Which slot(s) a custom flash should write to. CURRENT = the slot of the screen being used. */
    enum class CustomSlotTarget { CURRENT, SLOT_A, SLOT_B, BOTH }

    sealed class CustomPartitionResult {
        data class Ok(
            val blockDevice: ExtendedFile,
            val devicePath: String,
            val needsExtraWarning: Boolean
        ) : CustomPartitionResult()

        data class Rejected(val reason: String) : CustomPartitionResult()
    }

    private val customNameRegex = Regex("^[A-Za-z0-9_]{1,40}$")

    // Flashing these can hard brick the phone or wipe user data: always refused.
    private val blockedPartitionNames = setOf(
        "abl", "aop", "aop_config", "tz", "tzsc", "hyp", "keymaster", "km4", "devcfg",
        "imagefv", "uefisecapp", "qupfw", "shrm", "cpucp", "multiimgoem", "multiimgqti",
        "rpm", "pmic", "apdp", "cmnlib", "cmnlib64", "storsec", "bootloader", "ddr",
        "userdata", "super", "metadata", "persist", "misc", "frp", "devinfo",
        "modemst1", "modemst2", "fsg", "fsc", "ssd", "sec", "limits",
        // OnePlus device-unique calibration, modem NV backups and secure data
        "mdm1oemnvbktmp", "mdm_oem_stanvbk", "mdm_oem_dycnvbk", "oem_stanvbk", "oem_dycnvbk",
        "oplusstanvbk", "oplusdycnvbk", "param", "keystore", "secdata", "spunvm", "uefivarstore"
    )
    private val blockedPartitionPrefixes = listOf("xbl", "sbl", "gpt", "primarygpt", "backupgpt", "mdm1m9kefs")

    // Allowed, but the user gets an extra warning in the confirm dialog.
    private val warnPartitionNames = setOf(
        "modem", "dsp", "bluetooth", "cdt", "mdtp", "mdtpsecapp", "featenabler"
    )

    /**
     * Checks a user typed partition name and finds its block device for [slotSuffix].
     * Only real physical block devices are accepted (never logical/mapper partitions).
     * Reasons are plain text without slashes so they are safe in navigation routes.
     */
    fun resolveCustomPartition(rawName: String, slotSuffix: String): CustomPartitionResult {
        val name = rawName.trim()
        if (name.isEmpty()) {
            return CustomPartitionResult.Rejected("Type a partition name first")
        }
        if (!customNameRegex.matches(name)) {
            return CustomPartitionResult.Rejected("Use only letters, numbers and underscore")
        }
        val lower = name.lowercase()
        if (lower.endsWith("_a") || lower.endsWith("_b")) {
            return CustomPartitionResult.Rejected("Do not type _a or _b. The slot is added automatically")
        }
        if (lower in blockedPartitionNames || blockedPartitionPrefixes.any { lower.startsWith(it) }) {
            return CustomPartitionResult.Rejected("$name is protected and cannot be flashed here")
        }
        val manager = fileSystemManager
            ?: return CustomPartitionResult.Rejected("Not ready yet. Try again")

        val directories = listOfNotNull(
            bootParent?.path,
            "/dev/block/by-name",
            "/dev/block/bootdevice/by-name"
        ).distinct()

        // Slotted name first. The plain name is used only if no slotted one exists anywhere,
        // so an A/B partition can never end up on the wrong slot.
        val candidates = if (slotSuffix.isNotEmpty()) listOf("$name$slotSuffix", name) else listOf(name)
        for (candidate in candidates) {
            for (directory in directories) {
                val device = manager.getFile(directory, candidate)
                if (device.exists() && Shell.cmd("test -b $directory/$candidate").exec().isSuccess) {
                    return CustomPartitionResult.Ok(device, "$directory/$candidate", lower in warnPartitionNames)
                }
            }
        }
        return CustomPartitionResult.Rejected("Partition $name$slotSuffix was not found on this device")
    }

    /** Size of a block device in bytes, or null if it cannot be read. */
    fun blockDeviceSize(blockDevice: ExtendedFile): Long? {
        return Shell.cmd("wc -c < $blockDevice").exec().out.firstOrNull()?.trim()?.toLongOrNull()
    }

    /**
     * Copies the whole partition to /sdcard/KernelFlasher/custom_backups.
     * Returns the backup path, or null if the copy failed or is not the same size.
     */
    fun backupBlockDevice(blockDevice: ExtendedFile, label: String, expectedSize: Long): String? {
        val timestamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.US).format(java.util.Date())
        val directory = "/sdcard/KernelFlasher/custom_backups"
        val destination = "$directory/${label}_$timestamp.img"
        if (!Shell.cmd("mkdir -p $directory").exec().isSuccess) {
            return null
        }
        val copied = Shell.cmd("dd if=$blockDevice of=$destination bs=4096 && sync").exec().isSuccess
        val size = Shell.cmd("wc -c < $destination").exec().out.firstOrNull()?.trim()?.toLongOrNull()
        if (!copied || size != expectedSize) {
            Shell.cmd("rm -f $destination").exec()
            return null
        }
        return destination
    }

    /**
     * SHA-256 of the first [length] bytes of a block device, or null when the
     * device tools are unavailable (the caller then skips verification).
     */
    fun sha256OfBlockDevice(blockDevice: ExtendedFile, length: Long): String? {
        // Best effort: flush and drop the page cache so the read-back comes from storage rather than RAM.
        Shell.cmd("sync", "echo 3 > /proc/sys/vm/drop_caches").exec()
        val line = Shell.cmd("head -c $length $blockDevice | sha256sum").exec().out.firstOrNull() ?: return null
        val hash = line.trim().split(Regex("\\s+")).firstOrNull() ?: return null
        return if (Regex("^[0-9a-fA-F]{64}$").matches(hash)) hash.lowercase() else null
    }
}
