package com.project.kovid.extenstion

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.project.kovid.R

@BindingAdapter("binding:setImg")
fun setImg(view: ImageView, imgUri: String?){
    if(imgUri != null)
        Glide.with(view.context).load(imgUri).into(view)
    else
        view.setImageResource(R.drawable.ic_baseline_room_24)
}