package com.project.kovid.function.world

import android.os.Bundle
import android.view.View
import com.project.kovid.R
import com.project.kovid.base.BaseFragment
import com.project.kovid.databinding.FragmentWorldBinding

class WorldFragment : BaseFragment<FragmentWorldBinding>(R.layout.fragment_world) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}