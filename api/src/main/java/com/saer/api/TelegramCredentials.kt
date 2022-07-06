package com.saer.api

import org.drinkless.td.libcore.telegram.TdApi

object TelegramCredentials {
    val parameters = TdApi.TdlibParameters().apply {
        databaseDirectory = "/data/user/0/com.saer.telegramnew/files/"
        useMessageDatabase = false
        useSecretChats = false
        apiId = 16183956
        apiHash = "557f94e8df903daa3ca3db0ddb95a82d"
        useFileDatabase = true
        systemLanguageCode = "en"
        deviceModel = "Android"
        systemVersion = "Example"
        applicationVersion = "1.0"
        enableStorageOptimizer = true
//        useTestDc = true    // TODO(добавить BuildFlavors)
    }
}