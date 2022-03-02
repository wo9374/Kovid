package com.project.kovid.extenstion

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.project.kovid.R
import com.project.kovid.function.repository.MapRepository
import com.project.kovid.model.HospItem
import com.project.kovid.util.CanvasUtil


class CustomMarker(private val context: Context, map: GoogleMap, clusterManager: ClusterManager<HospItem>)
    : DefaultClusterRenderer<HospItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: HospItem, markerOptions: MarkerOptions) {
        val id: Int = when(item.recuClCd){
            MapRepository.HOSP_COMPREHENSIVE -> R.drawable.ic_hosp_comprehensive
            MapRepository.HOSP_GENERAL -> R.drawable.ic_hosp_general
            MapRepository.HOSP_DOCTOR_OFFICE -> R.drawable.ic_host_doctor_office
            else -> R.drawable.ic_hosp_comprehensive
        }

        val drawable = ContextCompat.getDrawable(context, id)
        val markerIcon: BitmapDescriptor = CanvasUtil.drawableToBitmapDescriptor(drawable!!)

        markerOptions.icon(markerIcon)
        markerOptions.snippet(item.snippet)
        markerOptions.title(item.title)
        super.onBeforeClusterItemRendered(item, markerOptions)
    }

    override fun onClusterItemRendered(clusterItem: HospItem, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
        marker.tag = clusterItem
    }
}