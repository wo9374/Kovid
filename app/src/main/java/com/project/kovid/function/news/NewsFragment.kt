package com.project.kovid.function.news

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
import com.project.kovid.headerLayout
import com.project.kovid.newsItemLayout
import com.project.kovid.objects.ContentsLoadingProgress

class NewsFragment : BaseFragment<FragmentNewsBinding>(R.layout.fragment_news) {
    private val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this@NewsFragment

        ContentsLoadingProgress.showProgress(this.javaClass.name, requireActivity(), true)

        viewModel.searchNewsApi()
        initLayout()
        subscribe(this)
    }

    private fun initLayout() {
        val linearLayoutManager = LinearLayoutManager(mContext)
        val gridLayoutManager = GridLayoutManager(mContext, 2)

        binding.epoxyRecycler.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)

            //구분선
            //addItemDecoration(DividerItemDecoration(mContext, linearLayoutManager.orientation))

            withModels {
                viewModel.newsData.value?.forEachIndexed { index, article ->
                   /* headerLayout {
                        id("header")
                        title("Covid News (Month)")
                        spanSizeOverride { totalSpanCount, position, itemCount -> 2 }
                    }*/

                    newsItemLayout {
                        id(index)
                        newsData(article)
                        onClickItem { bindingModel, parentView, clickedView, position ->
                            Log.d("Epoxy", "News 항목 $bindingModel, 부모 뷰 $parentView, 클릭뷰 $clickedView $position 눌러졌다")
                            navController.navigate(R.id.action_news_to_newsDetail)
                        }

                        if (index%3 ==0){
                            spanSizeOverride { totalSpanCount, position, itemCount -> 2 }
                        }else{
                            spanSizeOverride { totalSpanCount, position, itemCount -> 1 }
                        }
                    }
                }
            }//withModels

        }//binding.epoxyRecycler.apply
    }

    private fun subscribe(owner: LifecycleOwner) {
        viewModel.newsData.observe(owner) {
            binding.epoxyRecycler.requestModelBuild()
            ContentsLoadingProgress.hideProgress(this.javaClass.name) //Progress Hide
        }
    }
}