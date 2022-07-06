package com.saer.core.di

import android.app.Application
import com.saer.core.Resources
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreModule {

    @Provides
    @Singleton
    fun provideResources(application: Application): Resources = Resources.Base(application)
}