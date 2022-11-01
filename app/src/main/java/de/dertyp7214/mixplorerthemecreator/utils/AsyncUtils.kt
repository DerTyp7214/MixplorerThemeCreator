@file:Suppress("unused")

package de.dertyp7214.mixplorerthemecreator.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun doInBackground(doInBackground: () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        withContext(Dispatchers.Default) { doInBackground() }
    }
}

fun doIoInBackground(doInBackground: () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        withContext(Dispatchers.IO) { doInBackground() }
    }
}

inline fun runOnMain(crossinline run: () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        run()
    }
}

fun <T> doAsync(doInBackground: () -> T, getResult: (result: T) -> Unit = {}) {
    CoroutineScope(Dispatchers.Main).launch {
        withContext(Dispatchers.Default) {
            val result = doInBackground()
            CoroutineScope(Dispatchers.Main).launch {
                getResult(result)
            }
        }
    }
}

fun <T> doAsyncCallback(doInBackground: ((T) -> Unit) -> Unit, getResult: (result: T) -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        withContext(Dispatchers.Default) {
            doInBackground {
                CoroutineScope(Dispatchers.Main).launch {
                    getResult(it)
                }
            }
        }
    }
}

infix fun <T> (() -> T).asyncInto(into: (result: T) -> Unit) = doAsync(this, into)