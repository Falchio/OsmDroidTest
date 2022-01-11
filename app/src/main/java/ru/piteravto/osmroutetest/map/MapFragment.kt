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
import androidx.lifecycle.ViewModelProvider
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import ru.piteravto.osmroutetest.App
import ru.piteravto.osmroutetest.databinding.FragmentMapBinding
import java.io.File

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

        val points = TestData.getTestGeoPointList()
        val roadOverlay = Polyline().apply {
            setPoints(points)
            paint.strokeJoin = Paint.Join.ROUND //округляет соединение линий
            paint.strokeCap = Paint.Cap.ROUND   //округляет окончание линий на концах маршрута
            paint.strokeWidth = 5f
            paint.color = Color.RED
        }
        setupRoad(roadOverlay)

    }

    private fun setupRoad(roadOverlay: Polyline) {

        Log.e("MapFragment", "setupRoad: ${roadOverlay.numberOfPoints}")
        binding.map.overlays.add(roadOverlay)
        binding.map.invalidate()
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
        val defaultPosition = GeoPoint(59.962447, 30.441147)
        controller.setCenter(defaultPosition)
        controller.setZoom(9)
    }
}