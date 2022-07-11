package com.saer.core.di

import android.app.Application
import com.saer.core.Resources
import dagger.Module
import dagger.Provides

@Module
class CoreModule {

    @AppScope
    @Provides
    fun provideResources(application: Application): Resources = Resources.Base(application)
}