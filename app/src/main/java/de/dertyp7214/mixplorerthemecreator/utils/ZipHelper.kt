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
    fun zip(files: List<File>, zipFile: File, root: String = "") {
        val _files = arrayListOf<String>()
        fun addFiles(file: File) {
            if (!file.isDirectory) _files.add(file.absolutePath)
            else file.listFiles()?.forEach(::addFiles)
        }
        files.forEach(::addFiles)
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
                val fi = FileInputStream(_files[i])
                origin = BufferedInputStream(fi, buffer)
                val entry =
                    ZipEntry(_files[i].removePrefix(root).removePrefix("\\").removePrefix("/"))
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