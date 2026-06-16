package com.epms.epmssmartmirror.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import android.provider.MediaStore
import android.text.TextPaint
import android.text.TextUtils
import androidx.compose.ui.graphics.toArgb
import com.epms.epmssmartmirror.R
import com.epms.epmssmartmirror.data.AppData
import com.epms.epmssmartmirror.data.Profile
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileOutputStream

// ---------------------------------------------------------------------------
// Canon SELPHY postcard print geometry.
// After the perforated tabs are separated, the usable photo is 100 x 148 mm.
// We always print landscape, so the design canvas is 148 (wide) x 100 (tall) mm.
// ---------------------------------------------------------------------------
private const val PRINT_DPI = 300
private const val PRINT_LONG_MM = 148f
private const val PRINT_SHORT_MM = 100f
// The SELPHY prints borderless by bleeding a few mm past the cut line; keep the
// logo / text / QR inside this safe margin so they survive the tear-off.
private const val PRINT_BLEED_MM = 3f

val PRINT_WIDTH_PX = Math.round(PRINT_LONG_MM / 25.4f * PRINT_DPI)    // 1748
val PRINT_HEIGHT_PX = Math.round(PRINT_SHORT_MM / 25.4f * PRINT_DPI)  // 1181
val PRINT_SAFE_INSET_FRACTION = PRINT_BLEED_MM / PRINT_SHORT_MM       // 0.03

fun generateQrBitmap(content: String, size: Int): Bitmap? {
    return try {
        val hints = hashMapOf<EncodeHintType, Any>(EncodeHintType.MARGIN to 1)
        val bits = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bmp.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        bmp
    } catch (e: Exception) {
        null
    }
}

/** Draws [photo] center-cropped to completely fill the [destW] x [destH] canvas. */
private fun drawPhotoCropped(canvas: Canvas, photo: Bitmap, destW: Int, destH: Int) {
    val srcRatio = photo.width.toFloat() / photo.height
    val dstRatio = destW.toFloat() / destH
    val src = if (srcRatio > dstRatio) {
        // Photo is wider than the canvas -> crop the sides.
        val cropW = (photo.height * dstRatio).toInt()
        val x = (photo.width - cropW) / 2
        Rect(x, 0, x + cropW, photo.height)
    } else {
        // Photo is taller than the canvas -> crop top/bottom.
        val cropH = (photo.width / dstRatio).toInt()
        val y = (photo.height - cropH) / 2
        Rect(0, y, photo.width, y + cropH)
    }
    canvas.drawBitmap(photo, src, Rect(0, 0, destW, destH), Paint().apply { isFilterBitmap = true })
}

/**
 * Builds the branded composite.
 *
 * By default the output keeps the source photo's dimensions (used for gallery
 * saves). For printing, pass [outputWidth]/[outputHeight] set to the SELPHY
 * postcard size so the design matches the paper's aspect ratio exactly — the
 * photo is then center-cropped to fill, and [safeInsetFraction] pushes the
 * logo / text / QR inward so the borderless bleed doesn't clip them.
 */
fun createCompositeBitmap(
    context: Context,
    photo: Bitmap,
    profile: Profile,
    outputWidth: Int = photo.width,
    outputHeight: Int = photo.height,
    safeInsetFraction: Float = 0f
): Bitmap {
    val width = outputWidth
    val height = outputHeight
    val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)

    // Draw the photo center-cropped (fill) so it covers the canvas without
    // distortion regardless of the requested aspect ratio.
    drawPhotoCropped(canvas, photo, width, height)

    // Extra inset (px) to keep edge-anchored elements out of the SELPHY bleed.
    val inset = minOf(width, height) * safeInsetFraction

    val accentArgb = profile.accent.toArgb()

    // ---------- Radial vignette ----------
    val vignettePaint = Paint().apply {
        isAntiAlias = true
        shader = RadialGradient(
            width / 2f, height / 2f,
            (maxOf(width, height) * 0.78f),
            intArrayOf(Color.TRANSPARENT, Color.argb(115, 0, 0, 0)),
            floatArrayOf(0.55f, 1f),
            Shader.TileMode.CLAMP
        )
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), vignettePaint)

    // ---------- Top gradient strip + logo + website ----------
    val topH = height * 0.10f
    val topPaint = Paint().apply {
        shader = LinearGradient(
            0f, 0f, 0f, topH,
            Color.argb(205, 0, 0, 0),
            Color.argb(0, 0, 0, 0),
            Shader.TileMode.CLAMP
        )
    }
    canvas.drawRect(0f, 0f, width.toFloat(), topH, topPaint)

    try {
        val logo = BitmapFactory.decodeResource(context.resources, R.drawable.logo_epms)
        val lh = (height * 0.058f).toInt()
        val lw = (logo.width.toFloat() / logo.height * lh).toInt()
        val lx = (height * 0.022f + inset).toInt()
        val ly = (height * 0.022f + inset).toInt()
        canvas.drawBitmap(logo, null, Rect(lx, ly, lx + lw, ly + lh), null)

        val siteP = Paint().apply {
            color = Color.parseColor("#F8FAFD")
            textSize = height * 0.022f
            isAntiAlias = true
            isFakeBoldText = true
            setShadowLayer(6f, 0f, 2f, Color.argb(180, 0, 0, 0))
        }
        canvas.drawText(
            AppData.WEBSITE,
            (lx + lw + 18).toFloat(),
            (ly + lh * 0.72f),
            siteP
        )
    } catch (_: Exception) { /* logo not critical */ }

    // ---------- Bottom moldure ----------
    val overlayH = height * 0.34f
    val overlayTop = height - overlayH
    val bottomPaint = Paint().apply {
        shader = LinearGradient(
            0f, overlayTop, 0f, height.toFloat(),
            Color.argb(220, 11, 31, 59),
            Color.argb(245, 5, 12, 26),
            Shader.TileMode.CLAMP
        )
    }
    canvas.drawRect(0f, overlayTop, width.toFloat(), height.toFloat(), bottomPaint)

    // Accent stripe (8px) with subtle glow
    val accentGlow = Paint().apply {
        shader = LinearGradient(
            0f, overlayTop - 16f, 0f, overlayTop + 8f,
            Color.argb(0, accentArgb shr 16 and 0xFF, accentArgb shr 8 and 0xFF, accentArgb and 0xFF),
            Color.argb(110, accentArgb shr 16 and 0xFF, accentArgb shr 8 and 0xFF, accentArgb and 0xFF),
            Shader.TileMode.CLAMP
        )
    }
    canvas.drawRect(0f, overlayTop - 16f, width.toFloat(), overlayTop + 8f, accentGlow)
    canvas.drawRect(
        0f, overlayTop, width.toFloat(), overlayTop + 10f,
        Paint().apply { color = accentArgb }
    )

    // ---------- Emoji badge ----------
    val emojiR = height * 0.062f
    val emojiCx = (height * 0.030f + inset) + emojiR
    val emojiCy = overlayTop + (overlayH * 0.30f)

    // Soft halo
    val haloPaint = Paint().apply {
        isAntiAlias = true
        shader = RadialGradient(
            emojiCx, emojiCy, emojiR * 1.9f,
            intArrayOf(
                Color.argb(160, accentArgb shr 16 and 0xFF, accentArgb shr 8 and 0xFF, accentArgb and 0xFF),
                Color.TRANSPARENT
            ),
            floatArrayOf(0f, 1f),
            Shader.TileMode.CLAMP
        )
    }
    canvas.drawCircle(emojiCx, emojiCy, emojiR * 1.9f, haloPaint)

    val badgePaint = Paint().apply {
        isAntiAlias = true
        color = accentArgb
    }
    canvas.drawCircle(emojiCx, emojiCy, emojiR, badgePaint)
    canvas.drawCircle(
        emojiCx, emojiCy, emojiR,
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 4f
            color = Color.argb(120, 255, 255, 255)
        }
    )

    val emojiPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textSize = emojiR * 1.1f
    }
    val fm = emojiPaint.fontMetrics
    canvas.drawText(
        profile.emoji,
        emojiCx,
        emojiCy - (fm.ascent + fm.descent) / 2f,
        emojiPaint
    )

    // ---------- Text block ----------
    val textLeft = emojiCx + emojiR + 24f
    val textTop = overlayTop + (overlayH * 0.18f)

    // Calculate available text width (leave space for the QR card on the right)
    val qrSizePx = (overlayH * 0.62f).toInt()
    val qrPad = (height * 0.012f)
    val qrRight = width - (height * 0.055f) - inset
    val qrLeft = qrRight - qrSizePx - qrPad * 2
    val textRight = qrLeft - 24f
    val textWidth = (textRight - textLeft).coerceAtLeast(80f)

    val nameP = TextPaint().apply {
        color = accentArgb
        textSize = height * 0.040f
        isFakeBoldText = true
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
    }
    canvas.drawText(
        TextUtils.ellipsize(profile.name, nameP, textWidth, TextUtils.TruncateAt.END).toString(),
        textLeft, textTop + nameP.textSize, nameP
    )

    val headP = TextPaint().apply {
        color = Color.parseColor("#F8FAFD")
        textSize = height * 0.024f
        isAntiAlias = true
    }
    canvas.drawText(
        TextUtils.ellipsize(profile.headline, headP, textWidth, TextUtils.TruncateAt.END).toString(),
        textLeft, textTop + nameP.textSize + height * 0.052f, headP
    )

    val futP = TextPaint().apply {
        color = Color.parseColor("#FFC247")
        textSize = height * 0.024f
        isAntiAlias = true
        isFakeBoldText = true
    }
    canvas.drawText(
        TextUtils.ellipsize(profile.futureLine, futP, textWidth, TextUtils.TruncateAt.END).toString(),
        textLeft, textTop + nameP.textSize + height * 0.085f, futP
    )

    val msgP = TextPaint().apply {
        color = Color.parseColor("#C8D5E6")
        textSize = height * 0.020f
        isAntiAlias = true
    }
    canvas.drawText(
        TextUtils.ellipsize(AppData.FUTURE_MESSAGE, msgP, textWidth, TextUtils.TruncateAt.END).toString(),
        textLeft, textTop + nameP.textSize + height * 0.115f, msgP
    )

    // ---------- QR code on rounded white card ----------
    generateQrBitmap(AppData.QR_LINK, qrSizePx)?.let { qr ->
        val cardLeft = qrLeft
        val cardTop = overlayTop + (overlayH - (qrSizePx + qrPad * 2)) / 2f
        val cardRight = qrRight
        val cardBottom = cardTop + qrSizePx + qrPad * 2
        val r = 18f

        // Subtle shadow plate (manual since bitmap canvas doesn't always render setShadowLayer well)
        canvas.drawRoundRect(
            RectF(cardLeft + 3f, cardTop + 6f, cardRight + 3f, cardBottom + 6f),
            r, r,
            Paint().apply {
                color = Color.argb(140, 0, 0, 0)
                isAntiAlias = true
            }
        )

        canvas.drawRoundRect(
            RectF(cardLeft, cardTop, cardRight, cardBottom),
            r, r,
            Paint().apply {
                color = Color.WHITE
                isAntiAlias = true
            }
        )

        canvas.drawBitmap(
            qr, null,
            RectF(
                cardLeft + qrPad,
                cardTop + qrPad,
                cardRight - qrPad,
                cardBottom - qrPad
            ),
            null
        )
    }

    return output
}

fun saveCompositeToCache(context: Context, photo: Bitmap, profile: Profile): File {
    return saveBitmapToCache(
        context,
        createCompositeBitmap(
            context, photo, profile,
            outputWidth = PRINT_WIDTH_PX,
            outputHeight = PRINT_HEIGHT_PX,
            safeInsetFraction = PRINT_SAFE_INSET_FRACTION
        )
    )
}

fun saveBitmapToCache(context: Context, bitmap: Bitmap, filename: String = "epms_share.jpg"): File {
    val file = File(context.cacheDir, filename)
    FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 92, it) }
    return file
}

fun saveToGallery(context: Context, bitmap: Bitmap, suffix: String = ""): Uri? {
    val filename = "EPMS_${System.currentTimeMillis()}${suffix}.jpg"
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/EPMS Future Mirror")
    }
    val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    uri?.let { context.contentResolver.openOutputStream(it)?.use { s -> bitmap.compress(Bitmap.CompressFormat.JPEG, 95, s) } }
    return uri
}
