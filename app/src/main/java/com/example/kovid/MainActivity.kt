package com.example.kovid

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.kovid.base.BaseActivity
import com.example.kovid.databinding.ActivityMainBinding
import com.example.kovid.home.HomeViewModel

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModels()
    //by viewModels() 사용 안할때
    //val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel

        setNavigationInit()
    }

    private fun setNavigationInit() {
        val navHostFragment = supportFragmentManager.findFragmentById(binding.mainNavHost.id) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(
            binding.bottomNavigationView, navController
        )

        //특정 프래그먼트에서 BottomNavi 숨기기용
        navController.addOnDestinationChangedListener { _, destination, _ ->
            /*when(destination.id){
                R.id.homeFragment,
                R.id.favoriteFragment,
                R.id.messageFragment,
                R.id.myPageFragment -> binding.bottomNavigationView.visibility = View.VISIBLE
                else -> binding.bottomNavigationView.visibility = View.GONE
            }*/
        }
    }
}