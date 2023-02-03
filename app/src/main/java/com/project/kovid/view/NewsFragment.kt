package com.project.kovid.function.news

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentNewsBinding
import com.project.kovid.databinding.FragmentNewsContainerBinding
import com.project.kovid.newsItemLayout
import com.project.kovid.viewmodel.NewsViewModel
import com.project.kovid.widget.extension.customview.ContentsLoadingProgress

//Navigation 스택관리 때문인 Container Fragment
class NewsContainerFragment : BaseFragment<FragmentNewsContainerBinding>(R.layout.fragment_news_container)

class NewsFragment : BaseFragment<FragmentNewsBinding>(R.layout.fragment_news) {

    lateinit var newsViewModel : NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = ViewModelProvider(requireActivity())[NewsViewModel::class.java]

        binding.viewModel = newsViewModel
        binding.lifecycleOwner = this@NewsFragment

        ContentsLoadingProgress.showProgress(this.javaClass.name, requireActivity(), true)

        initLayout()

        subscribe(this)
    }

    override fun onStart() {
        super.onStart()
        newsViewModel.searchNewsApi()
        newsViewModel.searchNaverNews()
    }

    private fun initLayout() {
        val linearLayoutManager = LinearLayoutManager(mContext)
        val gridLayoutManager = GridLayoutManager(mContext, 2)

        binding.epoxyRecycler.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)

            //addItemDecoration(DividerItemDecoration(mContext, linearLayoutManager.orientation)) //구분선

            withModels {
                newsViewModel.newsData.value?.forEachIndexed { index, article ->
                    /* headerLayout {
                         id("header")
                         title("Covid News (Month)")
                         spanSizeOverride { totalSpanCount, position, itemCount -> 2 }
                     }*/

                    newsItemLayout {
                        id(index)
                        newsData(article)
                        onClickItem { bindingModel, parentView, clickedView, position ->
                            Log.d("Epoxy", "News 항목 $bindingModel, 부모 뷰 $parentView, 클릭뷰 $clickedView position $position")
                            newsViewModel.newsDetailData = bindingModel.newsData()
                            navController.navigate(R.id.action_news_to_newsDetail)
                        }

                        if (index % 3 == 0) spanSizeOverride { totalSpanCount, position, itemCount -> 2 }
                        else spanSizeOverride { totalSpanCount, position, itemCount -> 1 }
                    }
                }
            }//withModels
        }//binding.epoxyRecycler.apply
    }

    private fun subscribe(owner: LifecycleOwner) {
        newsViewModel.newsData.observe(owner) {
            binding.epoxyRecycler.requestModelBuild()
            ContentsLoadingProgress.hideProgress(this.javaClass.name) //Progress Hide
        }

        newsViewModel.naverData.observe(owner){
            //네이버 뉴스 대기
        }
    }
}