package com.project.kovid.function.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.kovid.R
import com.project.kovid.areaCovidItem
import com.project.kovid.databinding.AreaViewpagerItemBinding
import com.project.kovid.model.AreaData

class ViewPagerAdapter(private val listData:ArrayList<ArrayList<AreaData>>) : RecyclerView.Adapter<ViewHolderPage>() {
    lateinit var binding: AreaViewpagerItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPage {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.area_viewpager_item, parent, false)
        return ViewHolderPage(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderPage, position: Int) {
        val viewHolder: ViewHolderPage = holder
        viewHolder.onBind(listData[position])
    }

    override fun getItemCount(): Int = listData.size
}

class ViewHolderPage(val binding: AreaViewpagerItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(dataList: ArrayList<AreaData>) {
        binding.areaItemEpoxy.apply {

            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL)) //구분선

            withModels {
                dataList.forEachIndexed { index, data ->
                    areaCovidItem {
                        id(index)
                        areaData(data)
                    }
                }
            }
        }
    }
}