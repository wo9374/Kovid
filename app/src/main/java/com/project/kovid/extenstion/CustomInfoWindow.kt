package com.project.kovid.extenstion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.project.kovid.databinding.CustomInfoWindowLayoutBinding

internal class CustomInfoWindow(private val context : Context) : GoogleMap.InfoWindowAdapter{
    lateinit var binding : CustomInfoWindowLayoutBinding

    override fun getInfoContents(marker: Marker): View {
        binding = CustomInfoWindowLayoutBinding.inflate(LayoutInflater.from(context))

        binding.txtMarkerTitle.text = marker.title
        binding.txtMarkerSnippet.text = marker.snippet

        return binding.root
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }
}