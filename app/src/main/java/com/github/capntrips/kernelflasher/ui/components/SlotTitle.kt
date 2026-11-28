package com.github.capntrips.kernelflasher.ui.components

import com.github.capntrips.kernelflasher.R

/**
 * Title for a slot card or screen. Phones without A/B slots (empty suffix)
 * get "Partition Info", so the word "Slot" is never shown to them.
 */
fun slotTitleRes(slotSuffix: String): Int = when (slotSuffix) {
    "_a" -> R.string.slot_a
    "_b" -> R.string.slot_b
    else -> R.string.partition_info
}
