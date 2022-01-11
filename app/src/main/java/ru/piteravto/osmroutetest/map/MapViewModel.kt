package ru.piteravto.osmroutetest.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import ru.piteravto.osmroutetest.App
import java.util.*

class MapViewModel : ViewModel() {
    private val mutableRoad: MutableLiveData<Polyline> = MutableLiveData()
    val road: LiveData<Polyline> get() = mutableRoad

    fun requestRoad() {
        viewModelScope.launch(Dispatchers.IO) {
            val startPoint = GeoPoint(59.962447, 30.441147)
            val endPoint = GeoPoint(60.962447, 40.441147)
            val waypoints: ArrayList<GeoPoint> = arrayListOf(startPoint, endPoint)
            val roadManager = OSRMRoadManager(App.context)
            val road = roadManager.getRoad(waypoints)
            val roadOverlay =  RoadManager.buildRoadOverlay(road)
            mutableRoad.postValue(roadOverlay)
        }
    }
}