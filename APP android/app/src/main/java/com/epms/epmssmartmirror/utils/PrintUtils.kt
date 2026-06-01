package com.epms.epmssmartmirror.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import androidx.print.PrintHelper

fun printPhoto(context: Context, bitmap: Bitmap, portrait: Boolean = false) {
    val activity = context as? Activity ?: return
    PrintHelper(activity).apply {
        scaleMode = PrintHelper.SCALE_MODE_FIT
        orientation = if (portrait) PrintHelper.ORIENTATION_PORTRAIT else PrintHelper.ORIENTATION_LANDSCAPE
        colorMode = PrintHelper.COLOR_MODE_COLOR
    }.printBitmap("EPMS Future Mirror", bitmap)
}
