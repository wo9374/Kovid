package com.project.kovid

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import com.project.kovid.base.BaseActivity
import com.project.kovid.databinding.ActivityMainBinding
import com.project.kovid.extenstion.customview.ContentsLoadingProgress
import com.project.kovid.function.map.MapsFragment


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModels()
    //by viewModels() 사용 안할때
    //val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController

    var waitTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel

        initLayout()

        subscribeUI()
    }

    private fun initLayout() {
        navHostFragment = supportFragmentManager.findFragmentById(binding.navMainFragment.id) as NavHostFragment
        navController = navHostFragment.navController

        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, binding.navMainFragment.id)
        navController.navigatorProvider += navigator
        navController.setGraph(R.navigation.nav_main)

        binding.bottomNavigationView.setupWithNavController(navController)

        //val currentFragment = supportFragmentManager.primaryNavigationFragment // keepState 현재 프래그먼트
    }

    private fun subscribeUI() {

    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - waitTime >=1500 ) {
            waitTime = System.currentTimeMillis()
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show()
        } else {
            finish() // 액티비티 종료
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == MapsFragment.TAG_CODE_PERMISSION_LOCATION) {
            if (grantResults.isNotEmpty()) {
                var permission = false

                for (grant in grantResults) {
                    permission = grant == PackageManager.PERMISSION_GRANTED
                }

                if (permission) viewModel.mapPermission.value = true
                else {
                    Toast.makeText(this, "설정에서 권한을 허가 해주세요.", Toast.LENGTH_SHORT).show()
                    ContentsLoadingProgress.hideProgress(this.javaClass.name)
                }
            }
        }
    }

}