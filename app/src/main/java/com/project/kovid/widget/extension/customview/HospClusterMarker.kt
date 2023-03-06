package com.project.kovid.widget.extension.customview

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.ljb.data.model.ClinicCluster
import com.ljb.domain.entity.Clinic
import com.project.kovid.R
import com.project.kovid.widget.util.CanvasUtil

class HospClusterMarker(private val context: Context, map: GoogleMap, clusterManager: ClusterManager<ClinicCluster>)
    : DefaultClusterRenderer<ClinicCluster>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: ClinicCluster, markerOptions: MarkerOptions) {
        val iconType : Int = when(item.clinicType){
            Clinic.CLINIC_SELECTIVE -> R.drawable.ic_hosp_doctor_office
            Clinic.CLINIC_TEMPORARY -> R.drawable.ic_hosp_general
            //ClinicCluster.CLINIC_DOCTOR_OFFICE -> R.drawable.ic_hosp_doctor_office
            else -> R.drawable.ic_hosp_doctor_office
        }

        val drawable = ContextCompat.getDrawable(context, iconType)
        val markerIcon: BitmapDescriptor = CanvasUtil.drawableToBitmapDescriptor(drawable!!)

        markerOptions.apply {
            icon(markerIcon)
            snippet(item.snippet)
            title(item.title)
            position(LatLng(item.lat,item.lng))
        }
        //super.onBeforeClusterItemRendered(item, markerOptions)
    }

    override fun onClusterItemRendered(clusterItem: ClinicCluster, marker: Marker) {
        //super.onClusterItemRendered(clusterItem, marker)
        marker.tag = clusterItem
    }
}