package com.project.kovid.function.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ljb.domain.entity.Area
import com.project.kovid.R
import com.project.kovid.databinding.AreaListItemBinding

class AreaListAdapter : ListAdapter<Area, AreaViewHolder>(DiffUtil()) {
    lateinit var binding: AreaListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.area_list_item, parent, false)
        return AreaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class AreaViewHolder(val binding: AreaListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(area: Area){
        binding.apply {
            areaCountry.text = area.countryName
            areaTotalCase.text = area.totalCase
            areaNewCase.text = area.newCase
        }
    }
}

class DiffUtil: DiffUtil.ItemCallback<Area>(){
    override fun areItemsTheSame(oldItem: Area, newItem: Area): Boolean {
        return (oldItem.countryName == newItem.countryName)
    }

    override fun areContentsTheSame(oldItem: Area, newItem: Area): Boolean {
        return (oldItem.countryName == newItem.countryName)
    }
}
