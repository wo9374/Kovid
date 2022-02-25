package com.project.kovid.function.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.cameraMoveEvents
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentMapBinding
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
            /*setOnMarkerClickListener {
                false
            }*/
        }

        lifecycleScope.launchWhenCreated { //카메라 이동 Collect
            mGoogleMap.cameraMoveEvents().collect {
                //Log.d("MapFragment", "카메라 이동중")
            }
        }

        subscribe(this)
    }

    @SuppressLint("MissingPermission")
    fun subscribe(owner: LifecycleOwner) {

        //현위치
        mapsViewModel.myLocation.observe(owner) {
            val latLng = LatLng(it.latitude, it.longitude)

            mGoogleMap.apply {
                moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
                addMarker {
                    position(latLng)
                    title("사용자")
                    snippet("현재 위치 GPS")
                }
                /*addCircle {
                    center(latLng)
                    radius(it.accuracy.toDouble())
                    strokeColor(resources.getColor(R.color.fab_green)) //테두리 Color
                    fillColor(resources.getColor(R.color.fab_green))    //원 안 Color
                }*/
                //isMyLocationEnabled = true
            }
        }

        mapsViewModel.hospData.observe(owner) {
            it?.forEachIndexed { index, hospPlace->
                val makerOptions = MarkerOptions()

                val drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_baseline_local_hospital_24)

                makerOptions
                    .position(hospPlace.latLng)
                    .title(hospPlace.yadmNm)
                    .snippet("${hospPlace.address}\n${hospPlace.address}")
                    .icon(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(drawable)))

                mGoogleMap.addMarker(makerOptions)
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
        if (mapsViewModel.permissionCheck())
            tedPermission()
        else
            mapsViewModel.startLocation()
    }

    private fun tedPermission() {
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
            ).check()
    }

    fun drawableToBitmap(drawable: Drawable?): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable?.intrinsicWidth ?: 200,
            drawable?.intrinsicHeight ?: 200,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable?.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable?.draw(canvas)
        return bitmap
    }
}