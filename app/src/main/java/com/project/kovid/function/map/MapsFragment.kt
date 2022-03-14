package com.project.kovid.function.map

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.cameraMoveEvents
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.project.kovid.MainViewModel
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentMapBinding
import com.project.kovid.extenstion.customview.HospMapInfoWindow
import com.project.kovid.extenstion.customview.HospClusterMarker
import com.project.kovid.model.HospDBItem
import com.project.kovid.model.HospItem
import kotlinx.coroutines.flow.collect


class MapsFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback {
    private val mapsViewModel: MapsViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var clusterManager: ClusterManager<HospDBItem>

    var TAG = MapsFragment::class.java.simpleName

    companion object {
        const val TAG_CODE_PERMISSION_LOCATION = 100
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapViewModel = mapsViewModel
        binding.lifecycleOwner = this

        binding.mapView.onCreate(savedInstanceState)
        subscribe(this)
    }


    @SuppressLint("MissingPermission", "PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        val seoul = LatLng(37.554891, 126.970814)
        mGoogleMap = googleMap

        clusterManager = ClusterManager(mContext, mGoogleMap)
        clusterManager.apply {
            renderer = HospClusterMarker(mContext, mGoogleMap, clusterManager)
            setOnClusterItemClickListener { hospItem ->
                return@setOnClusterItemClickListener false
            }
        }

        mGoogleMap.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 15F))

            mGoogleMap.setOnCameraIdleListener(clusterManager)
            mGoogleMap.setOnMarkerClickListener(clusterManager)

            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.isZoomControlsEnabled = true
        }

        lifecycleScope.launchWhenCreated {
            mGoogleMap.cameraMoveEvents().collect {
                //Log.d(TAG, "카메라 이동중")
            }
        }
    }

    fun subscribe(owner: LifecycleOwner) {
        mainViewModel.mapPermission.observe(owner) {
            if (it == true) {
                binding.mapView.getMapAsync(this)

                mapsViewModel.startLocation()

                if (mapsViewModel.getAll().value.isNullOrEmpty()) {
                    mapsViewModel.getHospData()
                }
            } else {
                permissionCheck()
            }
        }

        mapsViewModel.myLocation.observe(owner) {
            val latLng = LatLng(it.latitude, it.longitude)
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
        }

        mapsViewModel.symptomTestHospData.observe(owner) {
            it.forEachIndexed { index, hospDBItem ->
                clusterManager.markerCollection.setInfoWindowAdapter(HospMapInfoWindow(mContext))
                clusterManager.addItem(hospDBItem)
                clusterManager.cluster()
            }
        }
        /*mapsViewModel.symptomTestHospData.observe(owner) {
            it.forEachIndexed { index, hospMarker ->
                clusterManager.markerCollection.setInfoWindowAdapter(HospMapInfoWindow(mContext))
                clusterManager.addItem(hospMarker)
                clusterManager.cluster()

                *//*val drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_baseline_local_hospital_24)
                val latLng = LatLng(hospItem.YPosWgs84,hospItem.XPosWgs84)
                val makerOptions = MarkerOptions()
                makerOptions
                    .position(latLng)
                    .title(hospItem.yadmNm)
                    .snippet("상세정보")
                    .icon(
                        CanvasUtil.drawableToBitmapDescriptor(drawable!!)
                    )
                mGoogleMap.addMarker(makerOptions)*//*
            }
        }*/
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
        mapsViewModel.stopLocation()
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
        if (mapsViewModel.permissionNotAllowCheck()) {
            mainViewModel.mapPermission.value = true
        } else {
            //tedPermission()
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissions,
                TAG_CODE_PERMISSION_LOCATION
            )
        }
    }

    fun tedPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                mapsViewModel.startLocation()
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
            )
            .check()
    }
}