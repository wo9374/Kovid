package com.project.kovid.function.news.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.project.kovid.MainViewModel
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentNewsDetailBinding
import com.project.kovid.function.news.NewsViewModel

class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding>(R.layout.fragment_news_detail) {

    lateinit var newsViewModel : NewsViewModel

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = ViewModelProvider(requireActivity())[NewsViewModel::class.java]

        binding.detailNewsData = newsViewModel.newsDetailData
        binding.lifecycleOwner = this@NewsDetailFragment

        initLayout()

        mainViewModel.offBottomNavi()
    }

    private fun initLayout() {
        binding.backBtn.setOnClickListener {
            navController.popBackStack()
            mainViewModel.onBottomNavi()
        }
    }

}