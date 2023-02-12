package com.project.kovid.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.clustering.ClusterManager
import com.ljb.data.mapper.latitude
import com.ljb.data.mapper.longitude
import com.ljb.data.model.SelectiveCluster
import com.ljb.domain.UiState
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentMapBinding
import com.project.kovid.viewmodel.MapsViewModel
import com.project.kovid.widget.extension.customview.ContentsLoadingProgress
import com.project.kovid.widget.extension.customview.HospClusterMarker
import com.project.kovid.widget.extension.customview.HospMapInfoWindow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback {
    var TAG = MapsFragment::class.java.simpleName

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var clusterManager: ClusterManager<SelectiveCluster>

    private val mapsViewModel: MapsViewModel by viewModels()

    companion object {
        const val TAG_CODE_PERMISSION_LOCATION = 100
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mapView.onCreate(savedInstanceState)

        if (checkLocationPermission()) {
            binding.mapView.getMapAsync(this)
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permissions[0])) {
                Snackbar.make(binding.root, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("확인") {
                        ActivityCompat.requestPermissions(requireActivity(), permissions, TAG_CODE_PERMISSION_LOCATION)
                    }.show()
            } else {
                ActivityCompat.requestPermissions(requireActivity(), permissions, TAG_CODE_PERMISSION_LOCATION)
            }
        }
    }


    @SuppressLint("MissingPermission", "PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        val seoul = LatLng(37.554891, 126.970814)
        mGoogleMap = googleMap

        clusterManager = ClusterManager<SelectiveCluster>(mContext, mGoogleMap)
        clusterManager.apply {
            renderer = HospClusterMarker(mContext, mGoogleMap, clusterManager)
            markerCollection.setInfoWindowAdapter(HospMapInfoWindow(mContext))
            setOnClusterItemClickListener {
                return@setOnClusterItemClickListener false
            }
        }

        mGoogleMap.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 15F))

            setOnCameraIdleListener {
                clusterManager
                mapsViewModel.hospitalClusters.value.let {
                    if (it is UiState.Complete) visibleDisplayCluster(it.data)
                }
            }
            setOnMarkerClickListener(clusterManager)

            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.isZoomControlsEnabled = true
        }

        observeData()
    }

    private fun observeData() {
        lifecycleScope.launch {
            launch {
                mapsViewModel.currentLocation.collect {
                    val latLng = LatLng(it.latitude, it.longitude)
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))

                    ContentsLoadingProgress.showProgress(this@MapsFragment.javaClass.name, requireActivity(), true, getString(R.string.searching_sido, mapsViewModel.detailAddress.first))
                }
            }
            launch {
                mapsViewModel.hospitalClusters
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collectLatest {
                        when (it) {
                            is UiState.Loading -> {

                            }
                            is UiState.Complete -> {
                                visibleDisplayCluster(it.data)
                                ContentsLoadingProgress.hideProgress(this@MapsFragment.javaClass.name)
                            }
                            is UiState.Fail -> {
                                Toast.makeText(mContext, "네트워크 오류!", Toast.LENGTH_SHORT).show()
                                ContentsLoadingProgress.hideProgress(this@MapsFragment.javaClass.name)
                            }
                        }
                    }
            }
        }
    }

    private fun visibleDisplayCluster(clusterList: List<SelectiveCluster>) {
        clusterList.forEach { cluster ->
            //현재 화면의 보이는 위도 경도 바운드
            mGoogleMap.projection.visibleRegion.latLngBounds.apply {
                if (
                    (northeast.latitude >= cluster.latitude() && northeast.longitude >= cluster.longitude()) &&
                    (southwest.latitude <= cluster.latitude() && southwest.longitude <= cluster.longitude())
                ){
                    clusterManager.addItem(cluster)
                    clusterManager.cluster()
                }else{
                    clusterManager.removeItem(cluster)
                }
            }
        }

    }

    private fun checkLocationPermission(): Boolean =
        ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
        if (checkLocationPermission())
            mapsViewModel.startLocation()
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
}