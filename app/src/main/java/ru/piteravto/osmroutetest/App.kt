package ru.piteravto.osmroutetest

import android.app.Application

class App : Application() {

    init {
        instance = this
    }

    companion object {
        private lateinit var instance: App
        val context get() = instance.applicationContext
    }
}
