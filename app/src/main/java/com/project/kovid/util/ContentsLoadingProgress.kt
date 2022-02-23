package com.project.kovid.util

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.project.kovid.R

class ContentsLoadingProgress {
    companion object {
        const val LOADING_SCREEN_TAG = "contents_loading_screen"

        val mContentViewHashMap: HashMap<String, ViewGroup> = HashMap()

        @JvmStatic
        @JvmOverloads
        fun showProgress(key: String, activity: Activity, touchable: Boolean = false) {
            if(mContentViewHashMap.containsKey(key)) return//이미 노출 중이라면.

            val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
            mContentViewHashMap[key] = contentView

            val inflater = LayoutInflater.from(activity)
            inflater.inflate(R.layout.include_contents_loading_screen, null)?.let {

                it.isClickable = touchable
                contentView.addView(it)

                it.layoutParams?.apply {
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                }

                val loadingImageView = it.findViewById<ImageView>(R.id.vodlist_loading_pregross)

                val rotateAnim = AnimationUtils.loadAnimation(activity, R.anim.loading_image_rotate_anim)
                rotateAnim.fillAfter = true

                loadingImageView.animation = rotateAnim

                it.tag = LOADING_SCREEN_TAG
            }
        }

        @JvmStatic
        fun hideProgress(key: String) {
            if(mContentViewHashMap.containsKey(key)) {
                mContentViewHashMap[key]?.apply {

                    val loadingScreen: View? = findViewById<View>(R.id.loading_screen)

                    if(loadingScreen != null && loadingScreen.visibility == View.VISIBLE)
                        loadingScreen.visibility = View.GONE

                    removeView(findViewWithTag(LOADING_SCREEN_TAG))
                }
                mContentViewHashMap.remove(key)
            }
        }
    }
}