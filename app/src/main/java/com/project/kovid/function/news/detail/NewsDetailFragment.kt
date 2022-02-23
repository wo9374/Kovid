package com.project.kovid.function.news.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentNewsDetailBinding
import com.project.kovid.function.news.NewsViewModel

class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding>(R.layout.fragment_news_detail) {
    private val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this@NewsDetailFragment
    }
}