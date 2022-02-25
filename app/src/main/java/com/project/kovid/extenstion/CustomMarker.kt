package com.project.kovid.extenstion

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.project.kovid.R
import com.project.kovid.model.HospItem
import com.project.kovid.util.CanvasUtil


class CustomMarker(private val context: Context, map: GoogleMap, clusterManager: ClusterManager<HospItem>)
    : DefaultClusterRenderer<HospItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: HospItem, markerOptions: MarkerOptions) {
        val id: Int = R.drawable.ic_baseline_local_hospital_24
       /* if (item.bg === 1) {      // drawable 변경

        } else if (item.bg === 2) {

        } else if (item.bg === 3) {

        } else if (item.bg === 4) {

        }*/

        val drawable = ContextCompat.getDrawable(context, id)
        val markerIcon: BitmapDescriptor = CanvasUtil.drawableToBitmapDescriptor(drawable!!)

        markerOptions.icon(markerIcon)
        markerOptions.snippet(item.snippet)
        markerOptions.title(item.title)
        super.onBeforeClusterItemRendered(item, markerOptions)
    }

}