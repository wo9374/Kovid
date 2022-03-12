package com.project.kovid.function.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.kovid.R
import com.project.kovid.databinding.AreaCovidItemBinding
import com.project.kovid.model.AreaData

class ViewPagerRecycleAdapter(private val listData:ArrayList<AreaData>) : RecyclerView.Adapter<AreaRecycleHolder>() {
    lateinit var binding: AreaCovidItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaRecycleHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.area_viewpager_item, parent, false)
        return AreaRecycleHolder(binding)
    }

    override fun onBindViewHolder(holder: AreaRecycleHolder, position: Int) {
        val viewHolder: AreaRecycleHolder = holder
        viewHolder.onBind(listData[position])
    }

    override fun getItemCount(): Int = listData.size
}

class AreaRecycleHolder(val binding: AreaCovidItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: AreaData) {
        binding.areaData = data
    }
}