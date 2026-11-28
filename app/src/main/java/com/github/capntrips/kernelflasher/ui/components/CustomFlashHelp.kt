package com.github.capntrips.kernelflasher.ui.components

// Plain Kotlin (no Android or Compose) so the texts can be checked on their own.

private const val HELP_WITH_SLOTS =
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

// Phones without A/B slots never see the word slot or the A/B options.
private const val HELP_WITHOUT_SLOTS =
    "What it does\n" +
    "Flashes any .img file to a partition that is not in the normal list, for example dtb or logo.\n\n" +
    "How to use it\n" +
    "1. Tap Custom Partition and type the partition name.\n" +
    "2. Recommended: turn on Back up the partition first.\n" +
    "3. Choose your .img file. It must be a raw image, not a sparse image.\n" +
    "4. Read the confirm screen. It shows the exact partition. If anything looks wrong, cancel.\n" +
    "5. After flashing, check the log. It says Verified when the written data matches your image.\n\n" +
    "Safety\n" +
    "- The wrong image on the wrong partition can stop the phone from booting.\n" +
    "- Protected partitions such as xbl, abl, tz, hyp, persist, userdata and super are blocked.\n" +
    "- Only real partitions are accepted. Logical partitions such as system and vendor are not supported here.\n" +
    "- Backups are saved in /sdcard/KernelFlasher/custom_backups."

/** Help text for the "?" in the Custom Partition dialog. */
fun customFlashHelp(isAb: Boolean): String = if (isAb) HELP_WITH_SLOTS else HELP_WITHOUT_SLOTS

/** One line above the partition name field. */
fun customFlashNameHint(isAb: Boolean): String =
    if (isAb) "Type the partition name without _a or _b. Example: dtb"
    else "Type the partition name. Example: dtb"
