package com.saer.telegramnew.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.saer.telegramnew.R

inline fun <reified T : Fragment>getNavController(): TestNavHostController {
    val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
    val scenario = launchFragmentInContainer<T>(themeResId = R.style.Theme_TelegramNew)
    scenario.onFragment { fragment ->
        navController.setGraph(R.navigation.nav_graph)
        Navigation.setViewNavController(fragment.requireView(), navController)
    }
    return navController
}