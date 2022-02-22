package com.project.kovid.extenstion

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("binding:setImg")
fun setImg(view: ImageView, imgUri: String){
    
    Glide.with(view.context).load(imgUri).into(view)
}