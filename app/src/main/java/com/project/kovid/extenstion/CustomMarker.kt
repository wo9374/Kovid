package com.project.kovid.extenstion

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.project.kovid.R
import com.project.kovid.function.repository.MapRepository
import com.project.kovid.model.HospMarker
import com.project.kovid.util.CanvasUtil


class CustomMarker(private val context: Context, map: GoogleMap, clusterManager: ClusterManager<HospMarker>)
    : DefaultClusterRenderer<HospMarker>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: HospMarker, markerOptions: MarkerOptions) {
        val id: Int = when(item.spclAdmTyCd){
            MapRepository.CORONA_TEST -> R.drawable.ic_covid_test
            MapRepository.SYMPTOM_TEST -> R.drawable.ic_symptom_test
            else -> R.drawable.ic_comprehensive_hosp
        }

        val drawable = ContextCompat.getDrawable(context, id)
        val markerIcon: BitmapDescriptor = CanvasUtil.drawableToBitmapDescriptor(drawable!!)

        markerOptions.icon(markerIcon)
        markerOptions.snippet(item.snippet)
        markerOptions.title(item.title)
        super.onBeforeClusterItemRendered(item, markerOptions)
    }

}