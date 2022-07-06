package com.saer.api

import dagger.Module
import dagger.Provides
import kotlinx.telegram.core.TelegramFlow
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    fun telegramFlow(): TelegramFlow {
        val api = TelegramFlow()
        api.attachClient()
        return api
    }
}