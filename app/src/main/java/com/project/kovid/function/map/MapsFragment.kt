package com.project.kovid.function.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentMapBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.cameraMoveEvents
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kotlinx.coroutines.flow.collect

class MapsFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback {

    private val mapsViewModel: MapsViewModel by viewModels()

    private lateinit var mGoogleMap: GoogleMap

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

        permissionCheck()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapViewModel = mapsViewModel
        binding.lifecycleOwner = this

        mapsViewModel.getHospital()

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) { //GoogleMap Setting
        val seoul = LatLng(37.554891, 126.970814)

        mGoogleMap = googleMap
        mGoogleMap.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 15F)) //카메라 이동

            mapsViewModel.myLocation.observe(this@MapsFragment){
                val currentPlace = LatLng(it.placeLatitude,it.placeLongitude)
                val accuracy : Double = it.placeAccuracy.toDouble()  //Float Double 변환

                moveCamera(CameraUpdateFactory.newLatLngZoom(currentPlace, 15F)) //카메라 이동

                addMarker {   //사용자 현위치 마커 추가
                    position(currentPlace)
                    title("사용자")
                    snippet("현재 위치 GPS")
                    //icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_room_24))
                }
                /*addCircle{
                    center(currentPlace)
                    radius(accuracy)
                    strokeColor(resources.getColor(R.color.fab_green)) //테두리 Color
                    fillColor(resources.getColor(R.color.fab_green))    //원 안 Color
                }*/

                isMyLocationEnabled = true //내 위치 Marker 와 이동 버튼 표시
            }

            setOnMarkerClickListener {
                false
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
        if (mapsViewModel.permissionCheck())
            tedPermission()
        else
            mapsViewModel.getLocation()
    }

    private fun tedPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                mapsViewModel.getLocation()
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