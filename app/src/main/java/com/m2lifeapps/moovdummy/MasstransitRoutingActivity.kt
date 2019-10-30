package com.m2lifeapps.moovdummy

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.geometry.SubpolylineHelper
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.*
import com.yandex.runtime.Error
import com.yandex.mapkit.transport.masstransit.Transport
import android.util.Log


class MasstransitRoutingActivity : AppCompatActivity(), Session.RouteListener {


    private lateinit var mapView: MapView
    private var MAPKIT_API_KEY: String = "843679b6-ddc8-4f6a-a4ca-40c7ea099ce8"
    private var DISTANCE_API_KEY: String = "707ece9f-5aeb-493d-a882-a29ff4536aba"

    private val TARGET_LOCATION = Point(41.00527, 28.97696)

    private val ROUTE_START_LOCATION = Point(41.005933, 28.920045)
    private val ROUTE_END_LOCATION = Point(41.045933, 28.920045)


    private lateinit var mapObjects: MapObjectCollection
    private lateinit var mtRouter: MasstransitRouter

    override fun onCreate(savedInstanceState: Bundle?) {


        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(this)
        TransportFactory.initialize(this)

        setContentView(R.layout.map_example)
        super.onCreate(savedInstanceState)

        mapView = findViewById(R.id.mapview)

        mapView.map.move(
            CameraPosition(
                TARGET_LOCATION, 14.0f, 0.0F, 0.0f
            )
        )

        mapObjects = mapView.map.mapObjects.addCollection()

        val massTrans = MasstransitOptions(
            ArrayList<String>(),
            ArrayList<String>(),
            TimeOptions()
        )
        val points = ArrayList<RequestPoint>()
        points.add(RequestPoint(ROUTE_START_LOCATION, RequestPointType.WAYPOINT, null))
        points.add(RequestPoint(ROUTE_END_LOCATION, RequestPointType.WAYPOINT, null))
        mtRouter = TransportFactory.getInstance().createMasstransitRouter()
        mtRouter.requestRoutes(points, massTrans, this)

    }

    override fun onMasstransitRoutesError(p0: Error) {
        Log.d("error", p0.toString())

    }

    override fun onMasstransitRoutes(routes: List<Route>) {
        //if (routes.isNotEmpty()) {
        for (section in routes[0].sections) {
            drawSection(
                section.metadata.data,
                SubpolylineHelper.subpolyline(
                    routes[0].geometry, section.geometry
                )
            )
            // }
        }
    }

    private fun drawSection(
        data: SectionMetadata.SectionData,
        geometry: Polyline
    ) {

        val polyObject = mapObjects.addPolyline(geometry)
        if (data.transports != null) {
            for (transport in data.transports!!) {
                if (transport.line.style != null) {
                    polyObject.strokeColor = 0xFF000000.toInt()

                    return

                }
            }
            val knownVehicleTypes = HashSet<String>()
            knownVehicleTypes.add("bus")
            knownVehicleTypes.add("tramway")

            for (transport in data.transports!!) {
                val vehicleType = getVehicleType(transport, knownVehicleTypes)

                if (vehicleType == "bus") {
                    polyObject.strokeColor = 0xFF00FF00.toInt() // Green
                    return
                } else if (vehicleType == "tramway") {
                    polyObject.strokeColor = 0xFFFF0000.toInt()// Red
                    return
                }
                polyObject.strokeColor = 0xFF0000FF.toInt()
            }

        } else {
            polyObject.strokeColor =0xFF000000.toInt()
        }
    }

    private fun getVehicleType(
        transport: Transport,
        knownVehicleTypes: java.util.HashSet<String>
    ): String {
        for (type in transport.line.vehicleTypes) {
            if (knownVehicleTypes.contains(type)) {
                return type
            }
        }
        return null.toString()

    }
}