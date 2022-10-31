package de.dertyp7214.mixplorerthemecreator.utils

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

@Suppress("LocalVariableName")
object ZipHelper {
    private const val buffer = 80000
    fun zip(files: List<File>, zipFile: File) {
        val _files = arrayListOf<Pair<String, String>>()
        fun addFiles(file: File, root: String) {
            if (!file.isDirectory) _files.add(Pair(file.absolutePath, root))
            else file.listFiles()?.forEach { addFiles(it, root) }
        }
        files.forEach {
            addFiles(it, it.absolutePath)
        }
        try {
            var origin: BufferedInputStream?
            val dest = FileOutputStream(zipFile)
            val out = ZipOutputStream(
                BufferedOutputStream(
                    dest
                )
            )
            val data = ByteArray(buffer)
            for (i in _files.indices) {
                val fi = FileInputStream(_files[i].first)
                origin = BufferedInputStream(fi, buffer)
                val entry =
                    ZipEntry(
                        _files[i].first.removePrefix(_files[i].second).removePrefix("\\")
                            .removePrefix("/")
                    )
                out.putNextEntry(entry)
                var count: Int
                while (origin.read(data, 0, buffer).also { count = it } != -1) {
                    out.write(data, 0, count)
                }
                origin.close()
            }
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun unpackZip(path: File, zipFile: File): Boolean {
        ZipFile(zipFile).use { zip ->
            path.mkdirs()
            zip.entries().asSequence().forEach { entry ->
                zip.getInputStream(entry).use { input ->
                    FileOutputStream(File(path, entry.name)).use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
        return true
    }
}