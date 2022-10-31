package de.dertyp7214.mixplorerthemecreator.utils

import android.content.Context
import androidx.fragment.app.FragmentActivity
import de.dertyp7214.mixplorerthemecreator.R
import de.dertyp7214.mixplorerthemecreator.components.XMLFile
import java.io.File

class ThemeUtils(private val activity: FragmentActivity) {
    internal val context: Context = activity

    internal val properties = XMLFile(
        activity.resources
            .openRawResource(R.raw.properties)
            .bufferedReader().use { it.readText() }
    )

    fun getProperties() = properties.simpleMap()

    fun exportXml() {
        properties.writeFile(File(context.filesDir, "properties.xml").absolutePath)
    }
}