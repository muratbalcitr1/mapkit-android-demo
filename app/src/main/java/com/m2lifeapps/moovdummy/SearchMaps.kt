package com.m2lifeapps.moovdummy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.geometry.Point
import com.yandex.runtime.image.ImageProvider


class SearchMaps : AppCompatActivity(), Session.SearchListener, CameraListener {

    private var MAPKIT_API_KEY: String = "843679b6-ddc8-4f6a-a4ca-40c7ea099ce8"

    private lateinit var session: Session
    private lateinit var searchManager: SearchManager
    private lateinit var mapView: MapView
    private lateinit var editText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(this)
        SearchFactory.initialize(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        mapView = findViewById(R.id.mapviewSearch)
        editText = findViewById(R.id.search_edit)

        editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                submitQuery(editText.text.toString())
                true
            } else {
                false
            }
        }

        mapView.map.move(
            CameraPosition(Point(41.005745, 28.977114), 14.0f, 0.0f, 0.0f)
        )
        submitQuery(editText.text.toString());


    }

    fun submitQuery(query: String) {

        session = searchManager.submit(
            query,
            VisibleRegionUtils.toPolygon(mapView.map.visibleRegion),
            SearchOptions(),
            this
        )
    }

    override fun onSearchError(p0: Error) {

    }

    override fun onSearchResponse(p0: Response) {
        val mapObjects = mapView.map.mapObjects
        mapObjects.clear()
        for (geoLocationItem in p0.collection.children) {
            val resultLocation = geoLocationItem.obj!!.geometry[0].point
            if (resultLocation != null) {
                mapView.map.move(
                    CameraPosition(resultLocation, 14.0f, 0.0f, 0.0f)
                )
                mapObjects.addPlacemark(
                    resultLocation,
                    ImageProvider.fromResource(this, R.drawable.ic_car_hatchback_grey600_36dp)
                )
            }

        }
    }

    override fun onCameraPositionChanged(
        p0: Map,
        p1: CameraPosition,
        p2: CameraUpdateSource,
        p3: Boolean
    ) {
        if (p3) {
            submitQuery(editText.text.toString())
        }
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }
}
