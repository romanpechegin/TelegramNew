package com.saer.telegramnew

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnPreDrawListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.elevation.SurfaceColors
import com.saer.core.di.lazyViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by lazyViewModel {
        (application as App).appComponent.mainActivityViewModel().create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        window.navigationBarColor = SurfaceColors.SURFACE_0.getColor(this)

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(object : OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                return if (viewModel.isReady) {
                    content.viewTreeObserver.removeOnPreDrawListener(this)
                    val navHostFragment =
                        supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
                    val inflater = navHostFragment.navController.navInflater
                    val graph = inflater.inflate(R.navigation.nav_graph)
                    graph.setStartDestination(if (viewModel.isAuth) com.saer.login.R.id.login_graph else R.id.chats_graph)
                    navHostFragment.navController.graph = graph
                    true
                } else {
                    false
                }
            }
        })
    }
}