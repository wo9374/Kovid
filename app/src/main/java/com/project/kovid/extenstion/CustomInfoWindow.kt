package com.project.kovid.extenstion

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.project.kovid.R

internal class CustomInfoWindow(context : Context) : GoogleMap.InfoWindowAdapter{

    val mWindow = (context as Activity).layoutInflater.inflate(R.layout.custom_info_window_layout, null)

    fun rendowWindowText(marker: Marker, view: View) {

        val tvTitle = view.findViewById<TextView>(R.id.marker_title)
        val tvSnippet = view.findViewById<TextView>(R.id.txt_marker_snippet)

        tvTitle.text = marker.title
        tvSnippet.text = marker.snippet
    }

    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}