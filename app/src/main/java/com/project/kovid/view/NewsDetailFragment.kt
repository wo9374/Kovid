package com.project.kovid.view

import android.os.Bundle
import android.view.View
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
        }
    }


    private fun observeData() = lifecycleScope.launch {
        newsViewModel.newsDetail.collect {
            binding.detailNewsData = it
            bottomNaviEnabled(false)
        }
    }


    private fun bottomNaviEnabled(bool: Boolean) = lifecycleScope.launch {
        mainViewModel.botNaviViewVisibility.emit(bool)
    }
}