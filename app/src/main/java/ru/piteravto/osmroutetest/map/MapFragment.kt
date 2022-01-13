package ru.piteravto.osmroutetest.map

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import ru.piteravto.osmroutetest.App
import ru.piteravto.osmroutetest.R
import ru.piteravto.osmroutetest.databinding.FragmentMapBinding
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
        val pointsA = RouteTestData.getTestGeoPointList('A')
        val pointsB = RouteTestData.getTestGeoPointList('B')
        if (pointsA.isEmpty()) return

        val forwardOverlay = createRouteOverlay(pointsA, true)
        val backwardOverlay = createRouteOverlay(pointsB, false)
        binding.map.overlays.add(forwardOverlay)
        binding.map.overlays.add(backwardOverlay)

        addDirectionArrowToOverlay(pointsA, true)
        addDirectionArrowToOverlay(pointsB, false)

        val busStops = BusStopsTestData.getTestData()
        addBusStop(busStops)

        binding.map.invalidate()
    }

    private fun addBusStop(testData: List<BusStop>) {
        val mapView = binding.map
        /* от порядка добавления зависит порядок отображения */
        addBusStopIcons(testData, mapView)
        addBusStopsNames(testData, mapView)
    }

    private fun addBusStopsNames(
        testData: List<BusStop>,
        mapView: MapView,
    ) {
        testData.forEach {
            val textMarker = TextMarker.create(mapView, it.getGeoPoint(), it.name)
            mapView.overlays.add(textMarker)
        }
    }

    private fun addBusStopIcons(
        testData: List<BusStop>,
        mapView: MapView
    ) {
        testData.forEach {
            val marker = Marker(mapView).apply {
                val icon: Drawable = it.getBusIcon() ?: return@forEach
                setIcon(icon)
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                position = it.getGeoPoint()
                textLabelBackgroundColor = Color.TRANSPARENT
                textLabelFontSize = 6
                textLabelForegroundColor = Color.RED
                title = it.name
            }
            mapView.overlays.add(marker)
        }
    }

    private fun addDirectionArrowToOverlay(points: List<GeoPoint>, isForward: Boolean) {
        val mapView = binding.map

        for (i in points.indices step 10) {
            val j = i + 1
            if (j >= points.size) return

            val center = points[i]
            val target = points[j]

            val angle = center.getAngleTo(target)
            val drawableId =
                if (isForward) R.drawable.arrow_forward_dir else R.drawable.arrow_backward_dir
            val markerIcon = ResourcesCompat.getDrawable(App.resources, drawableId, null)

            val marker = Marker(mapView).apply {
                position = center
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                setIcon(markerIcon)
                rotation = angle
            }
            mapView.overlays.add(marker)
        }
    }

    private fun createRouteOverlay(points: List<GeoPoint>, isForwardDirection: Boolean): Polyline {
        val roadOverlay = Polyline().apply {
            setPoints(points)
            paint.strokeJoin = Paint.Join.ROUND // соединение линий
            paint.strokeCap = Paint.Cap.SQUARE   // окончание линий на концах маршрута
            paint.strokeWidth = 10f
            paint.color = if (isForwardDirection) Color.RED else Color.BLUE
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

            controller.apply {
                val defaultPosition = GeoPoint(59.96859468141684, 30.24835467338562)
                setCenter(defaultPosition)
                setZoom(19)
            }
        }
    }
}

