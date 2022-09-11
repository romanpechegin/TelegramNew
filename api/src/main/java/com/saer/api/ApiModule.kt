package com.saer.api

import com.saer.core.di.AppScope
import dagger.Module
import dagger.Provides

@Module
class ApiModule {

    @AppScope
    @Provides
    fun telegramFlow(): TelegramFlow {
        val api = TelegramFlow()
        api.attachClient()
        return api
    }
}