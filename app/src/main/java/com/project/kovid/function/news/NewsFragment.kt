package com.project.kovid.function.news

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentNewsBinding
import com.project.kovid.newsItemLayout

class NewsFragment : BaseFragment<FragmentNewsBinding>(R.layout.fragment_news) {
    private val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this@NewsFragment

        initLayout()
        subscribe(this)

        viewModel.searchNewsApi()
    }

    private fun initLayout() {
        val linearLayoutManager = LinearLayoutManager(mContext)

        binding.epoxyRecycler.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(mContext, linearLayoutManager.orientation))

            withModels {
                viewModel.newsData.value?.forEachIndexed { index, article ->
                    newsItemLayout {
                        id(index) //index 필요
                        news(article)
                        onClickItem { bindingModel, parentView, clickedView, position ->
                            Log.d("Epoxy", "News 항목 $bindingModel, 부모 뷰 $parentView, 클릭뷰 $clickedView $position 눌러졌다")
                        }
                    }
                }

            }//withModels
        }

    }

    private fun subscribe(owner: LifecycleOwner) {
        viewModel.newsData.observe(owner) {
            binding.epoxyRecycler.requestModelBuild()
        }
    }

}