package com.epms.epmssmartmirror.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.epms.epmssmartmirror.data.AppData
import com.epms.epmssmartmirror.data.Profile
import java.io.File

fun sendEmailWithPhoto(
    context: Context,
    recipientEmail: String,
    recipientName: String,
    shareFile: File,
    profile: Profile
) {
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        shareFile
    )

    val body = """
        Olá $recipientName,

        Segue em anexo a tua fotografia EPMS Future Mirror no perfil "${profile.name}".

        Até breve,
        ${AppData.SCHOOL_NAME}
        ${AppData.WEBSITE}
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/jpeg"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
        putExtra(Intent.EXTRA_SUBJECT, "A tua fotografia EPMS Future Mirror")
        putExtra(Intent.EXTRA_TEXT, body)
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Enviar por email"))
}
