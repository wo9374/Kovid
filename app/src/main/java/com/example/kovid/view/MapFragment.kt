package com.example.kovid.view

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.kovid.R
import com.example.kovid.databinding.FragmentMapBinding
import com.example.kovid.viewmodel.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentMapBinding
    lateinit var navController: NavController

    //activity 의 ViewModel 을 따름
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var currentPoint: Marker

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)

        activity?.let {
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(binding.root)

        binding.mapView.getMapAsync(this)
    }

    //GoogleMap Setting
    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        viewModel.myLocation.observe(this) {
            val markerOption = MarkerOptions().apply {
                position(LatLng(it.placeLongitude, it.placeLatitude))
                title("사용자")
                snippet("현재 위치 GPS")
                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_room_24))
            }

            //사용자 현위치 마커 추가
            currentPoint = mGoogleMap.addMarker(markerOption)!! //TODO  !! Null 위험성 유추되는곳

            mGoogleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(it.placeLongitude, it.placeLatitude), 15F)
            )
        }
    }

    fun test(){
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
}