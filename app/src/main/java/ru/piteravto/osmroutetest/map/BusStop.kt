package ru.piteravto.osmroutetest.map

import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import org.osmdroid.util.GeoPoint
import ru.piteravto.osmroutetest.App
import ru.piteravto.osmroutetest.R

data class BusStop(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val directionChar: Char
) {
    private val forwardChar = 'A'

    fun getGeoPoint(): GeoPoint {
        return GeoPoint(latitude, longitude)
    }

    fun getBusIcon(): Drawable? {
        val resId = when (directionChar) {
            forwardChar -> R.drawable.busstop_forward_dir
            else -> R.drawable.busstop_backward_dir
        }

        return ResourcesCompat.getDrawable(App.resources, resId, null)
    }

    fun anchorU(): Float {
        return when (directionChar) {
            forwardChar -> 0.5f
            else -> 0.5f
        }
    }

    fun anchorV(): Float {
        return when (directionChar) {
            forwardChar -> 2f
            else -> 2f
        }
    }

}