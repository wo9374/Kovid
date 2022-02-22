package com.project.kovid.function.news.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentNewsBinding
import com.project.kovid.databinding.FragmentNewsDetailBinding
import com.project.kovid.function.news.NewsViewModel
import com.project.kovid.headerLayout
import com.project.kovid.newsItemLayout
import com.project.kovid.objects.ContentsLoadingProgress

class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding>(R.layout.fragment_news_detail) {
    private val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this@NewsDetailFragment

        ContentsLoadingProgress.showProgress(this.javaClass.name, requireActivity(), true)

        //viewModel.searchNewsApi()
        //initLayout()
        //subscribe(this)
    }

    private fun initLayout() {
        val linearLayoutManager = LinearLayoutManager(mContext)
        val gridLayoutManager = GridLayoutManager(mContext, 2)

    }

    private fun subscribe(owner: LifecycleOwner) {
        viewModel.newsData.observe(owner) {

        }
    }
}