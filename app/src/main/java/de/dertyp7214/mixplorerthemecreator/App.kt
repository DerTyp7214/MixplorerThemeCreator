package de.dertyp7214.mixplorerthemecreator

import android.app.Application
import com.google.android.material.color.DynamicColors
import de.dertyp7214.colorutilsc.ColorUtilsC

class App: Application() {

    init {
        ColorUtilsC.init()
    }

    override fun onCreate() {
        super.onCreate()

        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}