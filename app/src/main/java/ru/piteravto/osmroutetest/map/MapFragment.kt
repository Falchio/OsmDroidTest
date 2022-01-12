package ru.piteravto.osmroutetest.map

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import ru.piteravto.osmroutetest.App
import ru.piteravto.osmroutetest.R
import ru.piteravto.osmroutetest.databinding.FragmentMapBinding
import ru.piteravto.osmroutetest.map.ExtensionFunction.calculateAngle
import java.io.File

private const val TAG = "MapFragment"

class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MapViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initMapConfiguration()
        _binding = FragmentMapBinding.inflate(inflater, container, false)


        return binding.root
    }

    private fun initMapConfiguration() {
        val context = App.context
        val osmDroidConfig = Configuration.getInstance()
        val baseOsmDroidFolder = File(context.cacheDir, "osmdroid")
        osmDroidConfig.osmdroidBasePath = baseOsmDroidFolder
        val tileCacheFolder = File(baseOsmDroidFolder, "tiles")
        osmDroidConfig.osmdroidTileCache = tileCacheFolder

        osmDroidConfig.load(
            context,
            PreferenceManager.getDefaultSharedPreferences(context)
        )
    }

    override fun onStart() {
        super.onStart()

        setupMapView()
//        viewModel = ViewModelProvider(this)[MapViewModel::class.java]
//        viewModel.road.observe(viewLifecycleOwner,{ roadOverlay -> setupRoad(roadOverlay) })
//        viewModel.requestRoad()
        setupOverlays()
    }

    private fun setupOverlays() {
        val points = TestData.getTestGeoPointList()
        if (points.isEmpty()) return

        val roadOverlay = createRouteOverlay(points)
        binding.map.overlays.add(roadOverlay)

        addMarkersToOverlay(points)

        binding.map.invalidate()
    }

    private fun addMarkersToOverlay(points: List<GeoPoint>) {
        val mapView = binding.map

        val firstPoint = points[0]
        val secondPoint = points[1]
        val angle = firstPoint.calculateAngle(secondPoint)
        Log.e(TAG, "addMarkersToOverlay: $angle")
        val markerIcon = MarkerIcon.getIcon(R.drawable.arrow, angle)

        val firstMarker = Marker(mapView).apply {
            position = points.first()
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            title = "FIRST"
            setIcon(markerIcon)
        }
        mapView.overlays.add(firstMarker)
    }

    private fun createRouteOverlay(points: List<GeoPoint>): Polyline {
        val roadOverlay = Polyline().apply {
            setPoints(points)
            paint.strokeJoin = Paint.Join.ROUND //округляет соединение линий
            paint.strokeCap = Paint.Cap.SQUARE   //округляет окончание линий на концах маршрута
            paint.strokeWidth = 10f
            paint.color = Color.RED
        }
        return roadOverlay
    }

    private fun setupMapView() {
        val mapView = binding.map
        mapView.apply {
            setTileSource(TileSourceFactory.MAPNIK)
            minZoomLevel = 6 // размер зума https://wiki.openstreetmap.org/wiki/Zoom_levels
            maxZoomLevel = 20
            setBuiltInZoomControls(true)
            setMultiTouchControls(true)
        }

        val controller = mapView.controller
        val defaultPosition = GeoPoint(59.96859468141684, 30.24835467338562)
        controller.setCenter(defaultPosition)
        controller.setZoom(19)
    }
}

