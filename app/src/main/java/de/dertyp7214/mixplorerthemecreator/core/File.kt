package de.dertyp7214.mixplorerthemecreator.core

import java.io.File
import kotlin.text.Charsets.UTF_8


fun File.readXML(string: String? = null): Map<String, Any> {
    val output = HashMap<String, Any>()

    val content = string ?: inputStream().use {
        it.bufferedReader().readText()
    }

    return content.readXML()
}