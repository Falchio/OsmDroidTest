package ru.piteravto.osmroutetest.map

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import ru.piteravto.osmroutetest.App

object MarkerIcon {
    fun getIcon(resId: Int, angle: Float): Drawable {
        val drawableIcon: Drawable =
            ResourcesCompat.getDrawable(App.resources, resId, null)
                ?: throw NullPointerException("resource cannot be NULL")

        val icon = drawableIcon.toBitmap()
        val rotatedIcon = rotateBitmap(icon, angle)
        return BitmapDrawable(App.resources, rotatedIcon)
    }

    private fun rotateBitmap(icon: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(angle) }

        val scaledBitmap = Bitmap.createScaledBitmap(icon, icon.width, icon.height, true)
        return Bitmap.createBitmap(
            scaledBitmap,
            0,
            0,
            scaledBitmap.width,
            scaledBitmap.height,
            matrix,
            true
        )
    }
}