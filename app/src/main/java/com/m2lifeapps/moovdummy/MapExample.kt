package com.m2lifeapps.moovdummy

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.Animation
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.geometry.Point
import kotlinx.android.synthetic.main.map_example.*

import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.geometry.Polygon
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError


class MapExample : AppCompatActivity(), DrivingSession.DrivingRouteListener {


    private lateinit var mapView: MapView
    private var MAPKIT_API_KEY: String = "843679b6-ddc8-4f6a-a4ca-40c7ea099ce8"
    private var DISTANCE_API_KEY: String = "707ece9f-5aeb-493d-a882-a29ff4536aba"
    private var ROUTE_START_LOCATION = Point(41.015137, 28.979530)
    private var ROUTE_END_LOCATION = Point(39.925533, 32.866287)


    private lateinit var mapObjects: MapObjectCollection
    private lateinit var drivingRouter: DrivingRouter
    private lateinit var drivingSession: DrivingSession


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(this)
        DirectionsFactory.initialize(this);

        setContentView(R.layout.map_example)

        mapView = findViewById(R.id.mapview)
        mapView.map.move(
            CameraPosition(
                ROUTE_START_LOCATION, 14.0f, 0.0F, 0.0f
            ),
            Animation(Animation.Type.SMOOTH, 5f), null
        )
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        mapObjects = mapView.map.mapObjects.addCollection()

        submitRequest()

    }


    override fun onStop() {
        super.onStop()
        mapview.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        mapview.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onDrivingRoutesError(error: Error) {
        var errorMessage = getString(R.string.unknown_error_message)

    }

    override fun onDrivingRoutes(routes: List<DrivingRoute>) {
        for (drivingRoute in routes) {
            mapObjects.addPolyline(drivingRoute.geometry)
        }
    }

    private fun submitRequest() {

        val option = DrivingOptions()
        val requestPoints = ArrayList<RequestPoint>()
        requestPoints.add(
            RequestPoint(
                ROUTE_START_LOCATION,
                RequestPointType.WAYPOINT,
                null
            )
        )
        requestPoints.add(
            RequestPoint(
                ROUTE_END_LOCATION,
                RequestPointType.WAYPOINT,
                null
            )
        )
        drivingSession = drivingRouter.requestRoutes(requestPoints, option, this)
    }

}

