package de.dertyp7214.mixplorerthemecreator.core

import android.graphics.Bitmap
import java.io.File

fun File.readXML(string: String? = null): Map<String, Any> {
    val content = string ?: inputStream().use {
        it.bufferedReader().readText()
    }

    return content.readXML()
}

fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
    delete()
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
}