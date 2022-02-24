package com.project.kovid

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import com.project.kovid.base.BaseActivity
import com.project.kovid.databinding.ActivityMainBinding
import com.project.kovid.function.home.HomeFragment
import com.project.kovid.function.map.MapsFragment
import com.project.kovid.function.news.NewsFragment
import com.project.kovid.function.world.WorldFragment

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModels()
    //by viewModels() 사용 안할때
    //val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

    lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel

        initLayout()

        subscribeUI()
    }
    private fun initLayout() {
        navHostFragment = supportFragmentManager.findFragmentById(binding.navMainFragment.id) as NavHostFragment

        val navController = navHostFragment.navController

        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, binding.navMainFragment.id)
        navController.navigatorProvider += navigator

        navController.setGraph(R.navigation.nav_main)

        binding.bottomNavigationView.setupWithNavController(navController)

        /*navController.addOnDestinationChangedListener{ _, destination, _ ->

        }*/
    }

    private fun subscribeUI() {
        viewModel.visibility.observe(this){
            if (it){
                binding.bottomNavigationView.visibility = View.VISIBLE
            }else{
                binding.bottomNavigationView.visibility = View.GONE
            }
        }
    }

    override fun onBackPressed() {
        when(navHostFragment.childFragmentManager.fragments[0]){
            is HomeFragment,
            is WorldFragment,
            is MapsFragment,
            is NewsFragment -> super.onBackPressed()
            else ->{
                supportFragmentManager.popBackStack()
                viewModel.onBottomNavi()
            }
        }

        /*super.onBackPressed()
        for(fragment: Fragment in supportFragmentManager.fragments) {
            if (fragment.isVisible) {
                when(fragment){
                    is HomeFragment,
                    is WorldFragment,
                    is MapsFragment,
                    is NewsFragment -> super.onBackPressed()
                    else -> {
                        supportFragmentManager.popBackStack()
                        viewModel.onBottomNavi()
                    }
                }
            }
        }*/

    }
}