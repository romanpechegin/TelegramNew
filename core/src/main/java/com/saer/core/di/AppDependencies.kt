package com.saer.core.di

import com.saer.core.Resources

interface AppDependencies {
    fun provideResources(): Resources
}