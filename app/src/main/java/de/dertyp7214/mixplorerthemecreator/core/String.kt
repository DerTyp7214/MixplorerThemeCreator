package de.dertyp7214.mixplorerthemecreator.core

import android.util.Log
import org.xml.sax.InputSource
import java.io.StringReader
import java.util.HashMap
import javax.xml.parsers.DocumentBuilderFactory

fun String.readXML(): Map<String, Any> {
    val output = HashMap<String, Any>()

    val map = try {
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
            InputSource(StringReader(this))
        ).getElementsByTagName("properties")
    } catch (e: Exception) {
        e.printStackTrace()
        return output
    }

    try {
        if (map.length > 0)
            for (item in map.item(0).childNodes) {
                if (item.nodeName != "set" && !item.nodeName.startsWith("#")) {
                    val key = item.attributes?.getNamedItem("key")?.nodeValue
                    val value = item.attributes?.getNamedItem("value")?.nodeValue?.let {
                        when (item.nodeName) {
                            "long" -> it.toLong()
                            "boolean" -> it.toBooleanStrict()
                            "float" -> it.toFloat()
                            "double" -> it.toDouble()
                            "integer", "int" -> it.toInt()
                            else -> it
                        }
                    }
                    if (key != null) output[key] = value ?: item.textContent ?: ""
                } else if (item.nodeName == "set") {
                    val name = item.attributes?.getNamedItem("name")?.nodeValue
                    val value = item.childNodes?.toList()?.filter { !it.nodeName.startsWith("#") }
                        ?.map {
                            if (it.nodeName == "string") "<string>${it.textContent}</string>" else "<${it.nodeName} value=\"${
                                item.attributes?.getNamedItem("value")?.nodeValue
                            }\" />"
                        }?.toSet() ?: setOf()
                    if (name != null) output[name] = value
                }
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return output
}

operator fun String.times(other: Number): String = repeat(other.toInt())