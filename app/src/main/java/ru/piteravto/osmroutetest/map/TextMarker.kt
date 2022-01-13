package ru.piteravto.osmroutetest.map

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.content.ContextCompat
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import ru.piteravto.osmroutetest.App
import ru.piteravto.osmroutetest.R

// TODO: нужно сделать переключение цветов текстового маркера под ночной режим
object TextMarker {

    fun createTextMarker(
        map: MapView,
        position: GeoPoint,
        text: String,
        textSize: Int = 8
    ): Marker {
        val paint = getPaint(textSize)
        val baseline = -paint.ascent() // ascent() is negative
        val height = (baseline + paint.descent() + dpToPx(4)).toInt()
        val width = (paint.measureText(text) + dpToPx(16)).toInt()
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawColor(Color.TRANSPARENT)
        val textCoordinateY = baseline + dpToPx(2)
        val textCoordinateX = dpToPx(8)
        canvas.drawText(text, textCoordinateX, textCoordinateY, paint)
        val draw: Drawable = BitmapDrawable(App.resources, image)

        val marker = Marker(map).apply {
            this.position = position
            setIcon(draw)
            setAnchor(0.5f, 2f) // смещение по Ox и Oy в % от иконки
            setInfoWindow(null)
            alpha = 0f
        }

        return marker
    }

    /** Подготовка холста */
    private fun getPaint(textSize: Int): Paint {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            setTextSize(dpToPx(textSize))
            /*  */
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

            setShadowLayer(
                5f,
                5f,
                5f,
                ContextCompat.getColor(App.context, R.color.gray_light)
            )
            color = ContextCompat.getColor(App.context, R.color.gray)
            textAlign = Paint.Align.LEFT
            alpha = 200
        }



        return paint
    }

    private fun dpToPx(dp: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            App.resources.displayMetrics
        )
    }
}