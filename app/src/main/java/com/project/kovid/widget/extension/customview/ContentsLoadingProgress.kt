package com.project.kovid.widget.extension.customview

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.project.kovid.R
import com.project.kovid.databinding.LayoutContentsLoadingBinding

class ContentsLoadingProgress {

    companion object {
        const val LOADING_SCREEN_TAG = "contents_loading_screen"

        lateinit var binding: LayoutContentsLoadingBinding

        private val mContentViewHashMap: HashMap<String, ViewGroup> = HashMap()

        @JvmStatic
        @JvmOverloads
        fun showProgress(key: String, activity: Activity, touchable: Boolean = false, loadingTxt : String ="") {
            if (mContentViewHashMap.containsKey(key)) return //이미 노출 중이라면.

            val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
            mContentViewHashMap[key] = contentView

            binding = LayoutContentsLoadingBinding.inflate(LayoutInflater.from(activity))

            binding.root.rootView.let {
                contentView.addView(it)

                it.apply {
                    isClickable = touchable
                    layoutParams?.apply {
                        width = ViewGroup.LayoutParams.MATCH_PARENT
                        height = ViewGroup.LayoutParams.MATCH_PARENT
                    }
                    tag = LOADING_SCREEN_TAG
                }

                val rotateAnim = AnimationUtils.loadAnimation(activity, R.anim.loading_image_rotate_anim)
                rotateAnim.fillAfter = true

                binding.loadingProgressImg.animation = rotateAnim

                if (loadingTxt.isEmpty()){
                    binding.loadingTxt.visibility = View.GONE
                }else{
                    binding.loadingTxt.text = loadingTxt
                }
            }
        }

        @JvmStatic
        fun hideProgress(key: String) {
            if (mContentViewHashMap.containsKey(key)) {
                mContentViewHashMap[key]?.apply {

                    if (binding.loadingScreen.visibility == View.VISIBLE)
                        binding.loadingScreen.visibility = View.GONE

                    removeView(findViewWithTag(LOADING_SCREEN_TAG))
                }
                mContentViewHashMap.remove(key)
            }
        }

    }

}