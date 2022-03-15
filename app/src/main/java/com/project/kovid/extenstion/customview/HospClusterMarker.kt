package com.project.kovid.extenstion.customview

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.project.kovid.R
import com.project.kovid.function.repository.MapRepository
import com.project.kovid.model.HospDBItem
import com.project.kovid.util.CanvasUtil


class HospClusterMarker(private val context: Context, map: GoogleMap, clusterManager: ClusterManager<HospDBItem>)
    : DefaultClusterRenderer<HospDBItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: HospDBItem, markerOptions: MarkerOptions) {
        val id: Int = when(item.recuClCd){
            MapRepository.HOSP_COMPREHENSIVE -> R.drawable.ic_hosp_comprehensive
            MapRepository.HOSP_GENERAL -> R.drawable.ic_hosp_general
            MapRepository.HOSP_DOCTOR_OFFICE -> R.drawable.ic_hosp_doctor_office
            else -> R.drawable.ic_hosp_comprehensive
        }

        val drawable = ContextCompat.getDrawable(context, id)
        val markerIcon: BitmapDescriptor = CanvasUtil.drawableToBitmapDescriptor(drawable!!)

        markerOptions.icon(markerIcon)
        //markerOptions.snippet(item.snippet)
        markerOptions.title(item.title)
        markerOptions.position(LatLng(item.YPosWgs84,item.XPosWgs84))
        //super.onBeforeClusterItemRendered(item, markerOptions)
    }

    override fun onClusterItemRendered(clusterItem: HospDBItem, marker: Marker) {
        //super.onClusterItemRendered(clusterItem, marker)
        marker.tag = clusterItem
    }
}