package de.dertyp7214.mixplorerthemecreator.utils

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import de.dertyp7214.mixplorerthemecreator.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream

class FileUtils(private val activity: FragmentActivity) {
    private var currentFile: File? = null

    private val exportResultLauncher =
        activity.registerForActivityResult(ActivityResultContracts.CreateDocument("*/*")) {
            if (it != null) {
                try {
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO) {
                            currentFile?.let { file ->
                                activity.contentResolver.openOutputStream(it)?.let { output ->
                                    FileInputStream(file).copyTo(
                                        output
                                    )
                                }
                            }
                        }
                    }
                } catch (_: Exception) {
                }
            }
        }

    fun exportTheme(file: File) {
        currentFile = file
        exportResultLauncher.launch(file.name)
    }

    fun shareTheme(file: File) {
        val uri = FileProvider.getUriForFile(
            activity,
            activity.packageName,
            file
        )
        ShareCompat.IntentBuilder(activity)
            .setStream(uri)
            .setType("application/mit")
            .intent
            .setAction(Intent.ACTION_SEND)
            .setDataAndType(uri, "application/mit")
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION).apply {
                activity.startActivity(
                    Intent.createChooser(
                        this,
                        activity.getString(R.string.share_theme)
                    )
                )
            }
    }
}