package com.project.kovid.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentNewsBinding
import com.project.kovid.databinding.FragmentNewsContainerBinding
import com.project.kovid.newsItemLayout
import com.project.kovid.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

//Navigation 스택 관리 때문인 Container Fragment
class NewsContainerFragment :
    BaseFragment<FragmentNewsContainerBinding>(R.layout.fragment_news_container)

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding>(R.layout.fragment_news) {
    private val newsViewModel: NewsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner


        //ContentsLoadingProgress.showProgress(this.javaClass.name, requireActivity(), true)

        initLayout()

        observeData(this)
    }

    private fun initLayout() {
        val linearLayoutManager = LinearLayoutManager(mContext)
        val gridLayoutManager = GridLayoutManager(mContext, 2)

        binding.epoxyRecycler.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            //addItemDecoration(DividerItemDecoration(mContext, linearLayoutManager.orientation)) //구분선
        }
    }

    private fun observeData(owner: LifecycleOwner) = lifecycleScope.launch {
        newsViewModel.naverNews.collectLatest { list ->
            binding.epoxyRecycler.withModels {
                list.forEachIndexed { index, news ->
                    newsItemLayout {
                        id(index)
                        newsData(news)
                        onClickItem { bindingModel, parentView, clickedView, position ->
                            Log.d(
                                "Epoxy",
                                "News 항목 $bindingModel, 부모 뷰 $parentView, 클릭뷰 $clickedView position $position"
                            )
                            newsViewModel.setNewsDetail(bindingModel.newsData())
                            navController.navigate(R.id.action_news_to_newsDetail)
                        }

                        /*if (index % 3 == 0) spanSizeOverride { totalSpanCount, position, itemCount -> 2 }
                        else spanSizeOverride { totalSpanCount, position, itemCount -> 1 }*/
                    }
                }
            }
            //binding.epoxyRecycler.requestModelBuild()
            //ContentsLoadingProgress.hideProgress(this.javaClass.name) //Progress Hide
        }
    }

}