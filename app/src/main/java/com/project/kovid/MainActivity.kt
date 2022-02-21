package com.project.kovid

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
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