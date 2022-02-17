package com.example.kovid.view

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.kovid.R
import com.example.kovid.databinding.ActivityMainBinding
import com.example.kovid.viewmodel.MainViewModel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()
    //by viewModels() 사용 안할때
    //val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        permissionCheck()

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

    private fun permissionCheck() {
        if (viewModel.permissionCheck())
            tedPermission()
        else
            viewModel.getLocation()
    }

    private fun tedPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                viewModel.getLocation()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@MainActivity, "설정에서 권한을 허가 해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setRationaleMessage("서비스 사용을 위해서 몇가지 권한이 필요합니다.")
            .setDeniedMessage("[설정] > [권한] 에서 권한을 설정할 수 있습니다.")
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).check()
    }
}