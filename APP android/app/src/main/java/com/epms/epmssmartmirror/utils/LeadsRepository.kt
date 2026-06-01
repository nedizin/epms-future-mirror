package com.epms.epmssmartmirror.utils

import android.content.Context
import com.epms.epmssmartmirror.data.Lead
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun saveLead(context: Context, lead: Lead) {
    val dir = context.getExternalFilesDir(null) ?: context.filesDir
    val file = File(dir, "leads_epms.csv")
    if (!file.exists()) {
        file.writeText("timestamp,profile,photo_style,name,school,year_level,contact,delivery_email,interest_area,saved_landscape,saved_square,saved_story,saved_print_landscape,saved_print_portrait\n")
    }
    fun esc(s: String) = "\"${s.replace("\"", "\"\"")}\""
    fun bool(v: Boolean) = if (v) "1" else "0"
    file.appendText(
        "${esc(lead.timestamp)},${esc(lead.profile)},${esc(lead.photoStyle)}," +
        "${esc(lead.name)},${esc(lead.school)},${esc(lead.yearLevel)}," +
        "${esc(lead.contact)},${esc(lead.deliveryEmail)},${esc(lead.interestArea)}," +
        "${bool(lead.savedLandscape)},${bool(lead.savedSquare)},${bool(lead.savedStory)}," +
        "${bool(lead.savedPrintLandscape)},${bool(lead.savedPrintPortrait)}\n"
    )
}

fun currentTimestamp(): String =
    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
