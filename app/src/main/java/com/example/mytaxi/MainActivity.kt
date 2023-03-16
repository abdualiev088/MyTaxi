package com.example.mytaxi

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.example.mytaxi.databinding.ActivityMainBinding
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var mapView: MapView? = null

    //    Location
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private var lat: Double = 0.0
    private var long: Double = 0.0

    val locationParent = LatLng(lat, long)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        binding = ActivityMainBinding.inflate(layoutInflater)
        mapView = binding.mapView
        setContentView(binding.root)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = MyLocationListener()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(ACCESS_FINE_LOCATION),
                    1
                )
            } else {
                startLocationUpdates()
            }
        } else {
            startLocationUpdates()
        }


        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { map ->
            // Set one of the many styles available
            map.setStyle(Style.OUTDOORS) { _ ->
                Style.MAPBOX_STREETS
                // Style.MAPBOX_STREETS | Style.SATELLITE etc...
            }
        }
        val serviceIntent = Intent(this, LocationService::class.java)
        startService(serviceIntent)

        binding.apply {
            burger.setOnClickListener {
                navigation.setNavigationItemSelectedListener {
                    when (it.itemId) {
                        R.id.acc -> Toast.makeText(
                            applicationContext,
                            "Account",
                            Toast.LENGTH_SHORT
                        ).show()
                        R.id.order -> Toast.makeText(
                            applicationContext,
                            "Orders",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    drawer.closeDrawer(GravityCompat.START)
                    true
                }
                drawer.openDrawer(GravityCompat.START)
            }

            buttonStart.setOnClickListener(View.OnClickListener {
                val forground = ForegroundService.startService(
                    this@MainActivity,
                    "Foreground service is running ..."
                )
                d("MyLog", "$forground")
            })
            buttonStop.setOnClickListener(View.OnClickListener {
                ForegroundService.stopService(this@MainActivity)
            })

//            Camera

        }

    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            2000,
            10f,
            locationListener
        )
    }

    private inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            location.let {
                lat = location.latitude
                long = location.longitude

                Toast.makeText(
                    applicationContext,
                    "Latitude: $lat, Longitude: $long",
                    Toast.LENGTH_SHORT
                ).show()

                val newLoc = LatLng(location.latitude, location.longitude)

                d("MyLog", "$newLoc")
                mapView?.getMapAsync { map ->
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(newLoc, 15.0),
                        3000
                    )
                    Style.OnStyleLoaded { addAnnotationToMap(newLoc, map) }
                }


            }
        }
        override fun onStatusChanged(provider : String?, status: Int, extras: Bundle?){}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startLocationUpdates()
            }
            else{
                Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addAnnotationToMap(myLocation: LatLng, map: MapboxMap) {
        val markerOptions = MarkerOptions()
            .position(myLocation)
            .title("My Location")
            .setSnippet("This is where I am!")
            .setIcon(IconFactory.getInstance(this).fromResource(R.drawable.car))

        map.addMarker(markerOptions)
    }

    public override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    public override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    public override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

//    Update camera
}