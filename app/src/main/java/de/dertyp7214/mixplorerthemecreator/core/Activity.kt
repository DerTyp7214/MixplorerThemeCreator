package de.dertyp7214.mixplorerthemecreator.core

import android.app.Activity
import android.content.DialogInterface
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Activity.openDialog(
    @LayoutRes layout: Int,
    cancelable: Boolean = true,
    block: View.(DialogInterface) -> Unit
): AlertDialog {
    val view = layoutInflater.inflate(layout, null)
    return MaterialAlertDialogBuilder(this)
        .setCancelable(cancelable)
        .setView(view)
        .create().also { dialog ->
            block(view, dialog)
            dialog.show()
        }
}