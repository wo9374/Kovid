package com.project.kovid

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.project.kovid.base.BaseActivity
import com.project.kovid.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModels()
    //by viewModels() 사용 안할때
    //val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel

        initLayout()
    }

    private fun initLayout() {
        val navHostFragment = supportFragmentManager.findFragmentById(binding.mainNavHost.id) as NavHostFragment
        val navController = navHostFragment.navController

        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, binding.mainNavHost.id)
        navController.navigatorProvider.addNavigator(navigator)
        navController.setGraph(R.navigation.nav_main)

        binding.bottomNavigationView.setupWithNavController(navController)

        //특정 프래그먼트에서 BottomNavi 숨기기용
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.homeFragment,
                R.id.worldFragment,
                R.id.mapFragment,
                R.id.newsFragment -> binding.bottomNavigationView.visibility = View.VISIBLE
                else -> binding.bottomNavigationView.visibility = View.GONE
            }
        }

        fun isDarkTheme(activity: Activity): Boolean {
            return activity.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }
    }
}