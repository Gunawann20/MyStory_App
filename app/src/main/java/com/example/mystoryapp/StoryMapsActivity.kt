package com.example.mystoryapp

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mystoryapp.databinding.ActivityStoryMapsBinding
import com.example.mystoryapp.preferences.UserPreference
import com.example.mystoryapp.viewmodel.StoryMapsViewModel
import com.google.android.gms.maps.model.MapStyleOptions

class StoryMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val storyMapViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[StoryMapsViewModel::class.java]
        storyMapViewModel.getStoryMaps(UserPreference(this).getUSer().token.toString())
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        val storyMapViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[StoryMapsViewModel::class.java]

        storyMapViewModel.stories.observe(this){
            for (story in it){
                mMap.addMarker(MarkerOptions().position(LatLng(story.lat as Double, story.lon as Double)).title(
                    story.name
                ))
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(it[0].lat as Double, it[0].lon as Double)))
        }
        setMapStyle()
    }
    private fun setMapStyle(){
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed")
            }
        }catch (exception: Resources.NotFoundException){
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }
    companion object {
        private const val TAG = "MapsActivity"
    }
}