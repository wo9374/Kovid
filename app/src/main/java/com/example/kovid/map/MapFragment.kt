package com.example.kovid.map

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.kovid.R
import com.example.kovid.base.BaseFragment
import com.example.kovid.databinding.FragmentMapBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addCircle
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.cameraMoveEvents
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kotlinx.coroutines.flow.collect

class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback {
    //activity 의 ViewModel 을 따름
    private val viewModel: MapViewModel by activityViewModels()

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var locationCallback: LocationCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

        permissionCheck()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapViewModel = viewModel
        binding.lifecycleOwner = this

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        binding.testBtn.setOnClickListener {
            viewModel.getLocation()
        }
    }

    //GoogleMap Setting
    override fun onMapReady(googleMap: GoogleMap) {
        val seoul = LatLng(37.554891, 126.970814)

        mGoogleMap = googleMap
        mGoogleMap.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 15F)) //카메라 이동

            viewModel.myLocation.observe(this@MapFragment){
                val currentPlace = LatLng(it.placeLatitude,it.placeLongitude)
                val accuracy : Double = it.placeAccuracy.toDouble()  //Float Double 변환

                moveCamera(CameraUpdateFactory.newLatLngZoom(currentPlace, 15F)) //카메라 이동

                addMarker {   //사용자 현위치 마커 추가
                    position(currentPlace)
                    title("사용자")
                    snippet("현재 위치 GPS")
                    //icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_room_24))
                }
                addCircle{
                    center(currentPlace)
                    radius(accuracy)
                    strokeColor(resources.getColor(R.color.fab_green)) //테두리 Color
                    fillColor(resources.getColor(R.color.fab_green))    //원 안 Color
                }
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

    private fun permissionCheck() {
        if (viewModel.permissionCheck())
            tedPermission()
        else
            viewModel.getLocation()
    }

    private fun tedPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                viewModel.getLocation()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(mContext, "설정에서 권한을 허가 해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setRationaleMessage("서비스 사용을 위해서 몇가지 권한이 필요합니다.")
            .setDeniedMessage("[설정] > [권한] 에서 권한을 설정할 수 있습니다.")
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).check()
    }
}