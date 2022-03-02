package com.project.kovid.function.news.detail

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.project.kovid.MainViewModel
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentNewsDetailBinding
import com.project.kovid.function.news.NewsViewModel


class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding>(R.layout.fragment_news_detail) {

    lateinit var newsViewModel: NewsViewModel

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.popBackStack()
                mainViewModel
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLayout()

        mainViewModel.botNaviViewVisibility.postValue(false)
    }

    private fun initLayout() {
        newsViewModel = ViewModelProvider(requireActivity())[NewsViewModel::class.java]

        binding.detailNewsData = newsViewModel.newsDetailData
        binding.lifecycleOwner = this@NewsDetailFragment

        binding.toolBarTitle.isSelected = true  //Marquee 처리

        binding.backBtn.setOnClickListener {
            navController.popBackStack()
            mainViewModel.botNaviViewVisibility.postValue(true)
        }
    }
}