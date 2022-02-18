package com.example.kovid.view

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.kovid.R
import com.example.kovid.base.BaseFragment
import com.example.kovid.databinding.FragmentMapBinding
import com.example.kovid.viewmodel.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.cameraMoveEvents
import kotlinx.coroutines.flow.collect

class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback {
    //activity 의 ViewModel 을 따름
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var mGoogleMap: GoogleMap

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
    }

    //GoogleMap Setting
    override fun onMapReady(googleMap: GoogleMap) {
        val seoul = LatLng(37.554891, 126.970814)

        mGoogleMap = googleMap
        mGoogleMap.apply {
            addMarker {   //사용자 현위치 마커 추가
                position(seoul)
                title("사용자")
                snippet("현재 위치 GPS")
                //icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_room_24))
            }
            moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 15F)) //카메라 이동

            viewModel.myLocation.observe(this@MapFragment){
                moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.placeLatitude,it.placeLongitude), 15F)) //카메라 이동
            }
        }

        //카메라 이동 Collect
        lifecycleScope.launchWhenCreated {
            mGoogleMap.cameraMoveEvents().collect {
                //Log.d("MapFragment", "카메라 이동중")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}