package ru.piteravto.osmroutetest.map

import android.location.Location
import org.osmdroid.util.GeoPoint

object ExtensionFunction {
    fun GeoPoint.calculateAngle(target: GeoPoint): Float {
        val center = this
        val centerLoc = Location("").apply {
            longitude = center.longitude
            latitude = center.latitude
        }

        val targetLoc = Location("").apply {
            longitude = target.longitude
            latitude = target.latitude
        }

        return centerLoc.bearingTo(targetLoc)
    }
}