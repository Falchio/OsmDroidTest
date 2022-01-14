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

object CustomMarker {

    fun createTextMarker(
        map: MapView,
        position: GeoPoint,
        text: String,
        textSize: Int = 8
    ): Marker {
        val paint = getPaint(textSize)
        val bitmap = createBitmapForText(paint, text)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT)
        val baseline = -paint.ascent()
        val textCoordinateY = baseline + dpToPx(2)
        val textCoordinateX = dpToPx(8)
        canvas.drawText(text, textCoordinateX, textCoordinateY, paint)
        val draw: Drawable = BitmapDrawable(App.resources, bitmap)

        val marker = Marker(map).apply {
            this.position = position
            setIcon(draw)
            setAnchor(
                Marker.ANCHOR_CENTER,
                -Marker.ANCHOR_BOTTOM
            ) // смещение по Ox и Oy в % от иконки
            setInfoWindow(null)
        }

        return marker
    }

    private fun createBitmapForText(
        paint: Paint,
        text: String
    ): Bitmap {
        val baseline = -paint.ascent() // ascent() is negative
        val height = (baseline + paint.descent() + dpToPx(4)).toInt()
        val width = (paint.measureText(text) + dpToPx(16)).toInt()
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    /** Подготовка холста */
    private fun getPaint(textSize: Int): Paint {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            setTextSize(dpToPx(textSize))
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            setShadowLayer(
                2f,
                2f,
                2f,
                ContextCompat.getColor(App.context, R.color.gray_light)
            )
            color = ContextCompat.getColor(App.context, R.color.gray)
            textAlign = Paint.Align.LEFT
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

    fun createBusIconMarker(map: MapView, busStop: BusStop): Marker {
        return Marker(map).apply {
            val icon: Drawable =
                busStop.getBusIcon()!! //внутри метода Marker.setIcon() есть проверка на null, поэтому ничего страшного случится не может
            setIcon(icon)
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            position = busStop.getGeoPoint()
            textLabelBackgroundColor = Color.TRANSPARENT
            textLabelFontSize = 6
            textLabelForegroundColor = Color.RED
            setInfoWindow(null)
        }
    }
}