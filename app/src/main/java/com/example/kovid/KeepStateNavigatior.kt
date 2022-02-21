package com.example.kovid

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

/**
 * Custom Navigation
 * @author Lee Jae Bum
 * */
@Navigator.Name("keep_state_fragment")
class KeepStateNavigator(private val context: Context, private val manager: FragmentManager, private val containerId: Int)
    : FragmentNavigator(context, manager, containerId) {

    override fun navigate(destination: Destination, args: Bundle?, navOptions: NavOptions?, navigatorExtras: Navigator.Extras?): NavDestination? {

        val tag = destination.id.toString()

        val transaction = manager.beginTransaction()

        var initialNavigate = false
        val currentFragment = manager.primaryNavigationFragment

        // primaryNavigationFragment 가 존재하면 기존 primaryFragment hide 처리 (재생성 방지)
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        } else {
            initialNavigate = true
        }

        var fragment = manager.findFragmentByTag(tag)      // 최초로 생성되는 fragment

        if (fragment == null) {
            val className = destination.className
            fragment = manager.fragmentFactory.instantiate(context.classLoader, className)
            transaction.add(containerId, fragment, tag)    // add로 fragment 최초 생성 (add)
        } else {

            transaction.show(fragment)  // 이미 생성되어 있던 fragment 라면 show
        }

        transaction.setPrimaryNavigationFragment(fragment) // destination fragment 를 primary 로 설정

        // transaction 관련 fragment 상태 변경 최적화
        transaction.setReorderingAllowed(true)
        transaction.commitNow()

        return if (initialNavigate) {
            destination
        } else {
            null
        }
    }
}