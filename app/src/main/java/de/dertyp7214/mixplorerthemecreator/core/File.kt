package de.dertyp7214.mixplorerthemecreator.core

import java.io.File


fun File.readXML(string: String? = null): Map<String, Any> {
    val content = string ?: inputStream().use {
        it.bufferedReader().readText()
    }

    return content.readXML()
}