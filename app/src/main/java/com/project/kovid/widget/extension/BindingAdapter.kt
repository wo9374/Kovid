package com.project.kovid.widget.extension

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.kovid.R
import java.util.*

@BindingAdapter("setNaviVisible")
fun setBottomVisible(bottomNavi: BottomNavigationView, boolean: Boolean){
    if (boolean) bottomNavi.visibility = View.VISIBLE
    else bottomNavi.visibility = View.GONE
}

@BindingAdapter("setImg")
fun setImg(view: ImageView, imgUri: String?){

    if(imgUri != null)
        Glide
            .with(view.context)
            .load(imgUri)
            .override(view.width)    //받아온 이미지 크기 조정
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) //캐싱된 리소스와 로드할 리소스가 같은 리소스일때 캐싱된 리소스 사용, gif 느릴 경우 사용
            .into(view)
    else
        view.setImageResource(R.drawable.ic_baseline_newwork_error_24)
}

/*

   Glide
            .with(view.context)
            .load(imgUri)
            .placeholder()   // 이미지를 보여주는 동안 보여줄 이미지 지정
            .error()         // 에러 났을때 이미지
            .fallback(R.drawable.img_file_no_img)  //load할 url이 null인 경우 등 비어있을 때 보여줄 이미지를 설정
            .asGif() //Gif 로딩시

            //gif 루프 한번 돌게
            .listener(new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                    if (resource != null) {
                        resource.setLoopCount(1);
                    }
                    return false;
                }
            })
            .override(view.width)    //받아온 이미지 크기 조정
            .centerCrop()
            .thumbnail(0.1f) //지정한 %비율 만큼 미리 이미지를 가져와서 보여줄 수 있음. ex)0.1f 는 실제 이미지 해상도의 10%로 흐리게 보여줌
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) //캐싱된 리소스와 로드할 리소스가 같은 리소스일때 캐싱된 리소스 사용, gif 느릴 경우 사용
            .into(view)
}*/