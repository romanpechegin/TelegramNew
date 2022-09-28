package com.saer.core.di

import android.app.Application
import com.saer.core.Resources
import com.saer.core.communications.CountriesCommunication
import com.saer.core.communications.CountryCommunication
import com.saer.core.mappers.CountryMapper
import dagger.Module
import dagger.Provides

@Module
class CoreModule {

    @AppScope
    @Provides
    fun provideResources(application: Application): Resources = Resources.Base(application)

    @Provides
    fun provideCountryMapper(): CountryMapper = CountryMapper()

    @Provides
    fun provideCountriesCommunication(): CountriesCommunication = CountriesCommunication.Base()

    @Provides
    fun provideCountryCommunication(): CountryCommunication = CountryCommunication.Base()
}