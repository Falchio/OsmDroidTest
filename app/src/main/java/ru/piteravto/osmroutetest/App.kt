package ru.piteravto.osmroutetest

import android.app.Application
import android.content.Context
import android.content.res.Resources

class App : Application() {

    init {
        instance = this
    }

    companion object {
        private lateinit var instance: App
        val context: Context get() = instance.applicationContext
        val resources: Resources get() = instance.resources
    }
}
