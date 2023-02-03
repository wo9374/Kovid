package com.project.kovid.widget.extension.customview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.project.kovid.databinding.CustomInfoWindowLayoutBinding
import com.project.kovid.model.HospDBItem
import com.project.kovid.model.HospItem

internal class HospMapInfoWindow(private val context : Context) : GoogleMap.InfoWindowAdapter{
    lateinit var binding : CustomInfoWindowLayoutBinding

    override fun getInfoContents(marker: Marker): View {
        val hospItem = marker.tag as HospDBItem

        binding = CustomInfoWindowLayoutBinding.inflate(LayoutInflater.from(context))

        binding.txtMarkerTitle.text = hospItem.yadmNm
        binding.txtMarkerAddr.text = hospItem.addr
        binding.txtMarkerTel.text = hospItem.telno

        return binding.root
    }

    override fun getInfoWindow(marker: Marker): View? {
        //기본 창(흰색 버블)을 사용해야 함을 나타내려면 null 반환
        return null
    }
}