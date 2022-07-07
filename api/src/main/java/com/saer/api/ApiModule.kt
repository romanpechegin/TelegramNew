package com.saer.api

import dagger.Module
import dagger.Provides
import kotlinx.telegram.core.TelegramFlow

@Module
class ApiModule {

    @Provides
    fun telegramFlow(): TelegramFlow {
        val api = TelegramFlow()
        api.attachClient()
        return api
    }
}