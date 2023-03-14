package com.project.kovid.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.clustering.ClusterManager
import com.ljb.data.mapper.latitude
import com.ljb.data.mapper.longitude
import com.ljb.data.model.PolygonData
import com.ljb.data.model.ClinicCluster
import com.ljb.data.model.KOREA_SIDO
import com.ljb.data.model.KOREA_SIGUNGU
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
    private lateinit var clusterManager: ClusterManager<ClinicCluster>
    private var mPolygon: ArrayList<Polygon> = arrayListOf()

    private val mapsViewModel: MapsViewModel by viewModels()
    companion object{
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        const val DENIED = "DENIED"
        const val EXPLAINED = "EXPLAINED"
    }

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val deniedList: List<String> = result.filter { !it.value }.map { it.key }
        when {
            deniedList.isNotEmpty() -> {
                val map = deniedList.groupBy { permission ->
                    if (shouldShowRequestPermissionRationale(permission))
                        DENIED
                    else
                        EXPLAINED
                }
                map[DENIED]?.let { showPermissionDialog() }
                map[EXPLAINED]?.let { showPermissionDialog(goSetting = true) }
            }
            else -> {
                mapsViewModel.startLocation()
                binding.mapView.getMapAsync(this)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mapView.onCreate(savedInstanceState)

        if (checkLocationPermission()){
            binding.mapView.getMapAsync(this)
            mapsViewModel.startLocation()
        }
        else
            activityResultLauncher.launch(permissions)
    }


    @SuppressLint("MissingPermission", "PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        val seoul = LatLng(37.554891, 126.970814)
        mGoogleMap = googleMap

        clusterManager = ClusterManager<ClinicCluster>(mContext, mGoogleMap)
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
                mapsViewModel.selectiveClusters.value.let {
                    if (it is UiState.Complete)
                        visibleDisplayCluster(it.data)
                }
                mapsViewModel.temporaryClusters.value.let {
                    if (it is UiState.Complete)
                        visibleDisplayCluster(it.data)
                }
            }
            setOnMarkerClickListener(clusterManager)

            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.isZoomControlsEnabled = true
        }

        binding.regionSpinner.apply {
            searchBtn.setOnClickListener {
                getSelectedItemPair().run {
                    if (mapsViewModel.detailAddress != this){
                        clusterManager.clearItems()
                        mapsViewModel.getDbDataLoading(first, second, true)
                    }
                }
            }
        }

        observeData()
    }

    private fun observeData() {

        lifecycleScope.launchWhenStarted {
            mapsViewModel.currentLocation.collect {location ->
                val latLng = LatLng(location.latitude, location.longitude)
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))

                with(mapsViewModel){
                    stopLocation()

                    openMapJsonFile()

                    if (checkInitialData())
                        getDbDataLoading(detailAddress.first, detailAddress.second)
                    else{
                        getInitialRemoteData()
                    }

                    progressState.emit(true)
                }
            }
        }

        lifecycleScope.launch {
            launch {
                mapsViewModel.progressState.collect {
                    if (it) {
                        with(mapsViewModel){
                            if (checkInitialData()){
                                detailAddress.run {
                                    ContentsLoadingProgress.showProgress(this@MapsFragment.javaClass.name, requireActivity(), true, getString(R.string.searching_sido, "$first $second"))
                                }
                            }
                            else ContentsLoadingProgress.showProgress(this@MapsFragment.javaClass.name, requireActivity(), true, getString(R.string.initial_clinic_data))
                        }
                    }else{
                        ContentsLoadingProgress.hideProgress(this@MapsFragment.javaClass.name)
                    }
                }
            }

            //Clinic Marker
            launch {
                mapsViewModel.selectiveClusters.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collectLatest {
                        when (it) {
                            is UiState.Loading -> {}
                            is UiState.Complete -> visibleDisplayCluster(it.data)
                            is UiState.Fail -> Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            launch {
                mapsViewModel.temporaryClusters.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collectLatest {
                        when (it) {
                            is UiState.Loading -> {}
                            is UiState.Complete -> visibleDisplayCluster(it.data)
                            is UiState.Fail -> Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            //Maps Polygon
            launch {
                mapsViewModel.polygonData.collectLatest { data ->
                    if (mPolygon.isNotEmpty())
                        mPolygon.forEach { it.remove() } //Polygon 객체의 remove

                    when(data.type){
                        PolygonData.MULTI_POLYGON -> {
                            (data.mapsPolygon as List<List<LatLng>>).forEachIndexed { index, list ->
                                val polygon = PolygonOptions()
                                    .addAll(list)
                                    .fillColor(Color.argb(100, 255, 240, 255))
                                    .strokeColor(R.color.purple_700)
                                    .strokeWidth(5.0f)
                                mPolygon.add(index, mGoogleMap.addPolygon(polygon))
                            }
                        }
                        PolygonData.POLYGON ->{
                            val list = (data.mapsPolygon as List<LatLng>)
                            val polygon = PolygonOptions()
                                .addAll(list)
                                .fillColor(Color.argb(100, 255, 240, 255))
                                .strokeColor(R.color.purple_700)
                                .strokeWidth(5.0f)
                            mPolygon.add(0, mGoogleMap.addPolygon(polygon))
                        }
                    }

                    val zoom = when(data.rankAddress){
                        in 13..16 -> 10.3F
                        in 17..18-> 11.1F
                        else -> 12F
                    }
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(data.centerLatLng, zoom))
                }
            }
        }
    }
    private fun openMapJsonFile(){
        mContext.assets.open(KOREA_SIDO).bufferedReader().use { it.readText() }.run {
            mapsViewModel.parsingSiDo(this)
        }
        mContext.assets.open(KOREA_SIGUNGU).bufferedReader().use { it.readText() }.run {
            mapsViewModel.parsingSiGunGu(this)
        }
    }

    private fun visibleDisplayCluster(clusterList: List<ClinicCluster>) {
        clusterList.forEach { cluster ->
            //현재 화면의 보이는 위도 경도 바운드
            mGoogleMap.projection.visibleRegion.latLngBounds.apply {
                if (
                    (northeast.latitude >= cluster.latitude() && northeast.longitude >= cluster.longitude()) &&
                    (southwest.latitude <= cluster.latitude() && southwest.longitude <= cluster.longitude())
                ) {
                    clusterManager.addItem(cluster)
                    clusterManager.cluster()
                } else {
                    clusterManager.removeItem(cluster)
                }
            }
        }

    }

    private fun checkLocationPermission(): Boolean =
        ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun showPermissionDialog(goSetting: Boolean = false) {
        Snackbar.make(binding.root, if (goSetting) getString(R.string.permission_setting) else getString(R.string.permission_check), Snackbar.LENGTH_INDEFINITE).setAction(
            if (goSetting) getString(R.string.move_setting)
            else getString(R.string.ok)
        ) {
            if (goSetting) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + requireActivity().packageName))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else
                activityResultLauncher.launch(permissions)
        }.show()
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
}