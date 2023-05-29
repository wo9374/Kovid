package com.project.kovid.view

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentNewsDetailBinding
import com.project.kovid.viewmodel.MainViewModel
import com.project.kovid.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding>(R.layout.fragment_news_detail) {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val newsViewModel: NewsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.popBackStack()
                bottomNaviEnabled(true)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLayout()

        observeData()
    }

    private fun initLayout() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            toolBarTitle.isSelected = true  //Marquee 처리
            backBtn.setOnClickListener {
                navController.popBackStack()
                bottomNaviEnabled(true)
            }
            topBtn.setOnClickListener{
                nestedScroll.fling(0)
                nestedScroll.scrollTo(0,0)
                appbar.setExpanded(true,true)
            }

            // 1. 웹뷰 클라이언트 연결 (로딩 시작/끝 받아 오기)
            webView.webViewClient = object : WebViewClient() {
                // 1) 로딩 시작
                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    if (favicon != null){
                        pBar.visibility = View.VISIBLE // 로딩이 시작되면 로딩바 보이기
                    }
                }

                // 2) 로딩 끝
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    pBar.visibility = View.GONE         // 로딩이 끝나면 로딩바 없애기
                    topBtn.visibility = View.VISIBLE    // Top 버튼 보이기
                }

                // 3) 외부 브라우저가 아닌 웹뷰 자체에서 url 호출
                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return true
                }
            }
            // 2. WebSettings: 웹뷰의 각종 설정을 정할 수 있다.
            val ws: WebSettings = webView.settings
            ws.javaScriptEnabled = true // 자바스크립트 사용 허가
        }
    }


    private fun observeData() = lifecycleScope.launch {
        newsViewModel.newsDetail.collect {
            binding.detailNewsData = it
            binding.webView.loadUrl(it.url)
            bottomNaviEnabled(false)
        }
    }


    private fun bottomNaviEnabled(bool: Boolean) = lifecycleScope.launch {
        mainViewModel.botNaviViewVisibility.emit(bool)
    }
}