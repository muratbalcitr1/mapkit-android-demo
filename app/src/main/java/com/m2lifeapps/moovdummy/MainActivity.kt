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
import com.yandex.mapkit.geometry.Point
import kotlinx.android.synthetic.main.map_example.*

import androidx.core.app.ActivityCompat
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationStatus
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.image.AnimatedImageProvider
import androidx.annotation.NonNull
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.opengl.Visibility
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import com.yandex.mapkit.geometry.Polygon
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.runtime.ui_view.ViewProvider


class MainActivity : AppCompatActivity(), LocationListener {


    private lateinit var mapView: MapView
    private var MAPKIT_API_KEY: String = "843679b6-ddc8-4f6a-a4ca-40c7ea099ce8"
    private var DISTANCE_API_KEY: String = "707ece9f-5aeb-493d-a882-a29ff4536aba"


    private var TARGET_LOCATION = Point(41.00527, 28.97696)
    private var TARGET_LOCATION2 = Point(41.005933, 28.920045)
    private var TARGET_LOCATION3 = Point(41.015933, 28.920045)
    private var TARGET_LOCATION4 = Point(41.025933, 28.920045)
    private var TARGET_LOCATION5 = Point(41.035933, 28.920045)
    private var TARGET_LOCATION6 = Point(41.045933, 28.920045)

    private var locationManager: LocationManager? = null
    private var longitute = 0.0
    private var latitute = 0.0

    private lateinit var sublayerManager: SublayerManager
    private lateinit var mapObjects: MapObjectCollection
    private lateinit var linear: LinearLayout
    @SuppressLint("MissingPermission", "ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(this)
        setContentView(R.layout.map_example)

        mapView = findViewById(R.id.mapview)
        linear = findViewById(R.id.linear)

        GetLocation()

        mapObjects = mapView.mapWindow.map.mapObjects
        mapObjects.addPlacemark(
            TARGET_LOCATION,
            ImageProvider.fromResource(this, R.drawable.ic_car_hatchback_black_48dp)
        )
        mapObjects.addPlacemark(
            TARGET_LOCATION2,
            ImageProvider.fromResource(this, R.drawable.ic_car_hatchback_black_48dp)
        )
        mapObjects.addPlacemark(
            TARGET_LOCATION3,
            ImageProvider.fromResource(this, R.drawable.ic_car_hatchback_black_48dp)
        )
        mapObjects.addPlacemark(
            TARGET_LOCATION4,
            ImageProvider.fromResource(this, R.drawable.ic_car_hatchback_black_48dp)
        )
        mapObjects.addPlacemark(
            TARGET_LOCATION5,
            ImageProvider.fromResource(this, R.drawable.ic_car_hatchback_black_48dp)
        )
        mapObjects.addPlacemark(
            TARGET_LOCATION6,
            ImageProvider.fromResource(this, R.drawable.ic_car_hatchback_black_48dp)
        )
        mapObjects.addTapListener { mapObject, point ->
            T1(mapObjects, point)
        }


    }


    fun T1(mapObject: MapObject, point: Point): Boolean {

        mapView.map.mapObjects.addPlacemark(point)
            .setView(ViewProvider(Toast_("adsasd", "asdasdas", Color.WHITE, point)))
        return true
    }

    private var list = ArrayList<Point>()

    fun Toast_(a: String, b: String, color: Int, point: Point): View {
        //  var inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = LayoutInflater.from(this).inflate(R.layout.car_selected, null)

        val aracyil = view.findViewById<TextView>(R.id.tvAracYil)
        val aracMarkaModel = view.findViewById<TextView>(R.id.tvAracMarkaModel)
        val btnAracSec = view.findViewById<Button>(R.id.btnAractikla)

        view.setBackgroundColor(color)
        aracMarkaModel.text = a
        aracyil.text = b
        btnAracSec.setOnClickListener {
            aracyil.text = "deneme"
            Toast.makeText(this, "murat", LENGTH_LONG).show()
        }

        list.add(point)

        return view

    }

    fun GetLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val longitude = location!!.longitude
        val latitude = location.latitude
        mapView.mapWindow.map.mapObjects.addPlacemark(
            Point(latitude, longitude),
            ImageProvider.fromResource(this, R.drawable.ic_car_hatchback_grey600_36dp)
        )
        mapView.map.move(
            CameraPosition(Point(latitude, longitude), 14.0f, 0.0F, 0.0f),
            //ImageProvider.fromResource(this, R.drawable.refresh)
            Animation(Animation.Type.SMOOTH, 5f), null
        )
    }

    fun createMapBaloon() {

    }

    override fun onLocationChanged(location: android.location.Location?) {
        latitute = location!!.latitude
        longitute = location.longitude

        mapView.map.move(
            CameraPosition(Point(latitute, longitute), 1.4F, 0F, 0f),
            //ImageProvider.fromResource(this, R.drawable.refresh)
            Animation(Animation.Type.SMOOTH, 5f), null
        )
        mapView.mapWindow.map.mapObjects.addPlacemark(
            Point(latitute, longitute),
            ImageProvider.fromResource(this, R.drawable.ic_car_hatchback_black_48dp)
        )
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

}

