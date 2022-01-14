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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BusStop

        if (name != other.name) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        return result
    }


}