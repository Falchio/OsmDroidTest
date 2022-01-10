package ru.piteravto.osmroutetest.map

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import ru.piteravto.osmroutetest.App
import ru.piteravto.osmroutetest.databinding.FragmentMapBinding
import java.io.File

class MapFragment: Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

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

        val mapView = binding.map
        mapView.setTileSource(TileSourceFactory.MAPNIK)
    }
}