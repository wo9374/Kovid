package com.project.kovid.function.news

import android.os.Bundle
import android.view.View
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentNewsBinding

class NewsFragment : BaseFragment<FragmentNewsBinding>(R.layout.fragment_news) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}