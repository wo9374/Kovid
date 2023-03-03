package com.project.kovid.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ljb.domain.entity.AreaCovid
import com.project.kovid.R
import com.project.kovid.databinding.ItemAreaListBinding

class AreaListAdapter : ListAdapter<AreaCovid, AreaViewHolder>(DiffUtil()) {
    lateinit var binding: ItemAreaListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_area_list, parent, false)
        return AreaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class AreaViewHolder(val binding: ItemAreaListBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(area: AreaCovid){
        binding.apply {
            areaCountry.text = area.countryName
            areaTotalCase.text = area.totalCase
            areaNewCase.text = area.newCase
        }
    }
}

class DiffUtil: DiffUtil.ItemCallback<AreaCovid>(){
    override fun areItemsTheSame(oldItem: AreaCovid, newItem: AreaCovid): Boolean {
        return (oldItem.countryName == newItem.countryName)
    }

    override fun areContentsTheSame(oldItem: AreaCovid, newItem: AreaCovid): Boolean {
        return (oldItem.countryName == newItem.countryName)
    }
}
