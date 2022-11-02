@file:Suppress("MemberVisibilityCanBePrivate")

package de.dertyp7214.mixplorerthemecreator.components

import android.graphics.Color
import androidx.annotation.ColorInt
import de.dertyp7214.mixplorerthemecreator.core.joinToString
import de.dertyp7214.mixplorerthemecreator.core.readXML
import de.dertyp7214.mixplorerthemecreator.core.times
import org.apache.commons.text.StringEscapeUtils
import java.io.File
import java.util.*

@Suppress("unused")
class XMLFile(
    val path: String? = null,
    private val initMap: Map<String, Any>? = null,
    initValues: Map<String, XMLEntry> = hashMapOf(),
    empty: Boolean = false
) {
    private val values: HashMap<String, XMLEntry> = HashMap(initValues)

    constructor(initMap: Map<String, Any>?) : this(null, initMap)
    constructor(initString: String?) : this(initString?.readXML())
    constructor(path: String, initString: String?) : this(path, initString?.readXML())

    init {
        if (!empty) path.let { if (it != null) File(it).readXML() else initMap ?: mapOf() }
            .forEach { (key, value) ->
                values[key] = XMLEntry[key, value]
            }
    }

    fun simpleMap(): Map<String, String> {
        return values.map {
            Pair(it.key, it.value.value)
        }.toMap()
    }

    fun getValue(name: String): XMLEntry? {
        return values[name]
    }

    fun setValue(value: XMLEntry) {
        values[value.key] = value
    }

    fun writeFile(path: String? = this.path) {
        if (path != null) File(path).writeText(toString())
    }

    fun forEach(block: (XMLEntry) -> Unit) = values.forEach { (_, entry) -> block(entry) }
    fun filter(predicate: (XMLEntry) -> Boolean) =
        XMLFile(initValues = values.filter { (_, entry) -> predicate(entry) })

    fun has(name: String) = values.containsKey(name)
    fun has(entry: XMLEntry) = has(entry.key)
    fun hasNot(entry: XMLEntry) = !has(entry)
    fun entryEquals(entry: XMLEntry) = values[entry.key]?.value == entry.value
    fun entryNotEquals(entry: XMLEntry) = !entryEquals(entry)
    fun remove(name: String) = values.remove(name)
    operator fun get(name: String): Any? = values[name]?.value
    operator fun set(name: String, value: Any?) =
        if (value == null) remove(name) else values[name]?.setValue(value)

    val entries: List<XMLEntry>
        get() = values.values.toList()

    val site: Int
        get() = values.size

    fun toString(comment: String = ""): String {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n${comment.let { if (it.isNotEmpty()) "<!--DERTYP7214:$comment-->\n" else "" }}<properties>\n${
            values.joinToString("\n") { it.value[4] }
        }\n</properties>"
    }

    override fun toString() = toString("")

    override fun equals(other: Any?): Boolean {
        if (other !is XMLFile) return false
        return other.values.size == values.size && other.values.filter { (_, entry) -> values[entry.key] == entry }.size == values.size
    }

    override fun hashCode(): Int {
        var result = path?.hashCode() ?: 0
        result = 31 * result + (initMap?.hashCode() ?: 0)
        result = 31 * result + values.hashCode()
        return result
    }
}

class XMLEntry(val key: String, _value: String) {
    var value: String = _value
        private set

    fun setValue(newValue: Any): XMLEntry {
        value = newValue.toString()
        return this
    }

    operator fun get(indent: Int) =
        "${" " * indent}<entry key=\"$key\">${StringEscapeUtils.escapeXml11(value)}</entry>"

    override fun toString() = this[0]

    override fun equals(other: Any?): Boolean {
        if (other !is XMLEntry) return false
        return other.value == value && other.key == key
    }

    @ColorInt
    fun getColor() = try {
        Color.parseColor(value)
    } catch (e: Exception) {
        Color.RED
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

    companion object {
        operator fun <T> get(name: String, value: T): XMLEntry {
            return XMLEntry(
                name,
                value.toString(),
            ).setValue(value as Any)
        }
    }
}