package de.dertyp7214.mixplorerthemecreator.utils

import android.content.Context
import androidx.fragment.app.FragmentActivity
import de.dertyp7214.mixplorerthemecreator.R
import de.dertyp7214.mixplorerthemecreator.components.XMLFile
import java.io.File
import java.io.FileOutputStream

@Suppress("unused")
class ThemeUtils(private val activity: FragmentActivity) {
    internal val context: Context = activity

    private val themePath = File(context.filesDir, "theme")

    internal var properties = XMLFile(
        activity.resources
            .openRawResource(R.raw.properties)
            .bufferedReader().use { it.readText() }
    )
        private set

    fun getProperties() = properties.simpleMap()

    fun get(name: String, default: String = "") =
        properties.getValue(name)?.value ?: default

    fun set(name: String, value: String) =
        properties.getValue(name)?.setValue(value)

    fun reset() {
        properties = XMLFile(
            activity.resources
                .openRawResource(R.raw.properties)
                .bufferedReader().use { it.readText() }
        )
    }

    fun clean() {
        themePath.deleteRecursively()
    }

    fun packTheme(name: String = "theme"): File? {
        if (!themePath.exists()) return null

        val zipFile = File(context.filesDir, "${name.ifEmpty { "theme" }}.mit")

        ZipHelper.zip(listOf(themePath), zipFile)

        return if (zipFile.exists()) zipFile else null
    }

    fun exportXml() {
        if (!themePath.exists()) themePath.mkdirs()
        properties.writeFile(File(themePath, "properties.xml").absolutePath)
    }

    fun exportFont() {
        val fontPath = File(themePath, "fonts")
        if (!fontPath.exists()) fontPath.mkdirs()
        context.resources.openRawResource(R.raw.roboto_mono_regular).use { input ->
            FileOutputStream(File(fontPath, "roboto_mono_regular.ttf")).use { output ->
                input.copyTo(output)
            }
        }
    }

    fun exportIcons(): File {
        val iconPath = File(themePath, "drawable")
        if (!iconPath.exists()) iconPath.mkdirs()

        val zipFile = File(iconPath, "icons.zip")
        context.resources.openRawResource(R.raw.icons).use { input ->
            FileOutputStream(zipFile).use { output ->
                input.copyTo(output)
            }
        }

        ZipHelper.unpackZip(iconPath, zipFile)
        zipFile.delete()

        return iconPath
    }
}