package com.saer.login.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.saer.login.R

inline fun <reified T : Fragment>getNavController(): TestNavHostController {
    val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
    val scenario = launchFragmentInContainer<T>()
    scenario.onFragment { fragment ->
        navController.setGraph(R.navigation.login_graph)
        Navigation.setViewNavController(fragment.requireView(), navController)
    }
    return navController
}